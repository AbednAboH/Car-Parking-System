package il.cshaifasweng.customerCatalogEntities;


import il.cshaifasweng.LocalDateAttributeConverter;
import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.MoneyRelatedServices.Transactions;
import il.cshaifasweng.ParkingLotEntities.Car;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
//@Inheritance(strategy =InheritanceType.JOINED)
@Getter
@Setter
public abstract class Subscription extends Transactions {
    public final int NUMBER_OF_DAYS = 7;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;

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

    @OneToMany(fetch=FetchType.LAZY,cascade =CascadeType.ALL,orphanRemoval = true)
    private List<Car> carsList=new ArrayList<>();
//    Should we get the cars by the customer? instead of redundantly retrieve the cars twice.
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
