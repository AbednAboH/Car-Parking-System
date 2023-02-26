package il.cshaifasweng.customerCatalogEntities;


import il.cshaifasweng.LocalDateAttributeConverter;
import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.MoneyRelatedServices.Transactions;
import il.cshaifasweng.ParkingLotEntities.Car;
import il.cshaifasweng.ParkingLotEntities.EntryAndExitLog;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
//@Inheritance(strategy =InheritanceType.JOINED)
@Getter
@Setter
public abstract class Subscription extends Transactions {
    public final int NUMBER_OF_DAYS = 7;


    @ManyToOne
    @JoinColumn(name = "registeredCustomer_id",nullable = false)
    private RegisteredCustomer registeredCustomer;

    @Column(name = "hoursPerMonth")
    private int hoursPerMonth;

    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name = "start_date")
    private LocalDate startDate;

    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name = "experation_date")
    private LocalDate expirationDate;

    @Column(name = "is_active")
    private boolean isActive=true;

    @Column(name = "allowed_days")
    private String allowedDays;
    @Column
    private String email;
    @OneToMany(fetch=FetchType.LAZY,cascade =CascadeType.ALL)
    private List<EntryAndExitLog> entryAndExitLogs=new ArrayList<>();

    @OneToMany(fetch=FetchType.LAZY,cascade =CascadeType.ALL,orphanRemoval = true)
    private List<Car> carsList=new ArrayList<>();

    @OneToMany(fetch=FetchType.LAZY,cascade =CascadeType.ALL,orphanRemoval = true)
    private List<Transactions> renewalsHistory=new ArrayList<>();
    @OneToOne(fetch=FetchType.LAZY,cascade =CascadeType.ALL,orphanRemoval = true)
    private OneTimePass oneTimePass;
    public void RenewContranct(String transactionMethode,double value){
        Transactions transactions=new Transactions();
        transactions.setTransaction_method(transactionMethode);
        transactions.setValue(value);
        transactions.setDate(LocalDate.now());
        transactions.setTransactionStatus(true);
        renewalsHistory.add(transactions);
        if (this.expirationDate.isBefore(LocalDate.now()))
            this.expirationDate=transactions.getDate().plusMonths(1);
        else
            this.expirationDate=this.expirationDate.plusMonths(1);

    }
    public boolean checkIfAllowed(){
        int i=LocalDateTime.now().getDayOfWeek().ordinal();
        //monday->0, thursday->1 .... sunday->6
        return allowedDays.charAt((i +1)% 7) == '1';

    }
    public boolean enteredToday(){
        if (getLatestLog()!=null)
            return getLatestLog().getAcutallEntryTime().toLocalDate().equals(LocalDate.now());
        else return false;
    }
    public void setEntryAndExitLogs(EntryAndExitLog entryAndExitLog){
        this.entryAndExitLogs.add(entryAndExitLog);
    }
    public EntryAndExitLog getLatestLog(){
        if (!entryAndExitLogs.isEmpty()){
            return entryAndExitLogs.stream().max(Comparator.comparing(EntryAndExitLog::getAcutallEntryTime)).get();
        }
        else return null;
    }
    public EntryAndExitLog getEntryAndExitLog(String carPlateNumber){
        for (EntryAndExitLog entryAndExitLog:
             entryAndExitLogs) {
            if (entryAndExitLog.getActiveCar().equals(carPlateNumber))
                return entryAndExitLog;
        }
        return null;
    }
    public void setEntryAndExitLog(String carPlateNumber,EntryAndExitLog entryExit){
        for (EntryAndExitLog entryAndExitLog:
             entryAndExitLogs) {
            if (entryAndExitLog.getActiveCar().equals(carPlateNumber))
                entryAndExitLog=entryExit;
        }

    }
    public  String getParkingLotIdAsString(){
        return "All";
    }
    public String getCarsAsString(){
        String cars="";
        int i=0;
        for (Car car:
             carsList) {
            cars=cars.concat(car.toString());
           if(i>0)
               cars.concat(", ");
           i++;
        }
        return cars;
    }
    public Car getCar(String lisePlate){
        for (Car car:
             carsList) {
            if(car.getCarNum().equals(lisePlate))
                return car;
        }
        return null;
    }
    public Subscription(Customer customer, int hoursPerMonth, LocalDate startDate,
                        LocalDate expirationDate, boolean isActive, String allowedDays,List<String> cars,
                        int value,String transaction_method,boolean transactionStatus) {
        this.hoursPerMonth = hoursPerMonth;
        this.startDate = startDate;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
        this.allowedDays = allowedDays;
        this.value=value;
        this.transaction_method=transaction_method;
        this.transactionStatus=transactionStatus;
        this.date=startDate;
        this.registeredCustomer=(RegisteredCustomer)customer;
        for(String car:cars)
            this.carsList.add(new Car(car));
        initCar();
    }
    public Subscription(Customer customer,int hoursPerMonth, LocalDate startDate, LocalDate expirationDate, boolean isActive, String allowedDays,List<String> cars) {
        this.hoursPerMonth = hoursPerMonth;
        this.startDate = startDate;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
        this.allowedDays = allowedDays;
        this.registeredCustomer=(RegisteredCustomer)customer;
        for(String car:cars)
            this.carsList.add(new Car(car));
        initCar();
    }
    public void initCar(){
        for (Car car:
             carsList) {
            car.setTransaction(this);
            car.setCustomer(registeredCustomer);
        }
    }
    public Subscription() {

    }


    public boolean[] getAllowedDays(){
        if(allowedDays.isBlank() || allowedDays.length() != 7) {
            throw new IllegalArgumentException("Allowed days string is not valid!"+allowedDays);
        }

        return toBooleanArray(allowedDays);
    }

    protected boolean[] toBooleanArray(String s){
        boolean[] array = new boolean[s.length()];
        for (int i = 0; i < s.length(); i++) {
            array[i] = s.charAt(i) == '1';
        }
        return array;
    }

    public RegisteredCustomer getRegisteredCustomer() {
        return registeredCustomer;
    }

    @Override
    public  String toString(){
        return "SubId="+id
                +", hours_per_month="+hoursPerMonth
                +", subscriptionDate="+startDate+
                ", experation="+expirationDate
                +", active" + isActive+
                ", allowedDays="+allowedDays;
    }

}
