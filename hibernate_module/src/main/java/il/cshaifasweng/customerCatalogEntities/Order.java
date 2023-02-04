package il.cshaifasweng.customerCatalogEntities;

import il.cshaifasweng.LocalDateAttributeConverter;
import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.MoneyRelatedServices.Transactions;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order extends Transactions {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="registeredCustomer_id")
    private RegisteredCustomer registeredCustomer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parkingLot_id")
    private ParkingLot parkingLotID;

    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name="dateOfOrder")
    private LocalDate dateOfOrder;

    @Column(name="active")
    private boolean active;

    @Column(name="enteringTime")
    private String entering;

    @Column(name="exitingTime")
    private String exiting;

    @Column(name="plateNumber")
    private String plateNum;

    @Column(name="email")
    private String email;
    @Column(name="ReminderSent")
    private boolean reminderSent=false;
    @Column(name="agreedToPayPenalty")
    private boolean agreedToPayPenalty=false;
    public Order(RegisteredCustomer registeredCustomer, ParkingLot parkingLotID, LocalDate date,
                 String entering, String exiting, String plateNum, String email) {
        this.registeredCustomer = registeredCustomer;
        this.dateOfOrder=date;
        this.date=LocalDate.now();
        this.parkingLotID = parkingLotID;
        this.entering = entering;
        this.exiting = exiting;
        this.plateNum = plateNum;
        this.email = email;
        this.active = true;
    }
    public Order(RegisteredCustomer registeredCustomer, ParkingLot parkingLotID, LocalDate date,
                 String entering, String exiting, String plateNum, String email,boolean localBuilder) {
        this.date=date;
        this.registeredCustomer = registeredCustomer;
        this.parkingLotID = parkingLotID;
        this.entering = entering;
        this.exiting = exiting;
        this.plateNum = plateNum;
        this.email = email;
        this.active = true;
        if (!localBuilder)
        this.registeredCustomer.addOrder(this);
    }
    public Order(ParkingLot parkingLotID, LocalDate date,
                 String entering, String exiting, String plateNum, String email) {
//        this.registeredCustomer = registeredCustomer;
        this.date=date;
        this.parkingLotID = parkingLotID;
        this.entering = entering;
        this.exiting = exiting;
        this.plateNum = plateNum;
        this.email = email;
        this.active = true;
    }
    @Override
    public String toString(){
        return "order id: "+id+" at"+date;
    }
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="reports_id")
//    private Reports reports;
    public Order() {

    }
    // TODO: 1/10/2023 toString Function 
}
