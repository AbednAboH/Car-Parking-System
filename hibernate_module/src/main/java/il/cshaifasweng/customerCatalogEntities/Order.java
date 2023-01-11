package il.cshaifasweng.customerCatalogEntities;

import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="registeredCustomer_id")
//    private RegisteredCustomer registeredCustomer;

    @Column(name = "registeredCustomer_id")
    private int registeredCustomerID;
    //    private RegisteredCustomer registeredCustomer;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="reports_id")
//    private Reports reports;
    @Column(name = "parkingLot_id")
    private int parkingLotID;

    @Column(name = "active")
    private boolean active;

    @Column(name = "enteringTimeAndDate")
    private SimpleDateFormat dateAndTimeToEnter;

    @Column(name = "leavingTimeAndDate")
    private SimpleDateFormat dateAndTimeToLeave;

    @Column(name = "carID")
    private String carID;

    @Column(name = "email")
    private String email;

    public Order() {

    }

    public Order(int registeredCustomerID, int parkingLotID, boolean active,
                 SimpleDateFormat dateAndTimeToEnter, SimpleDateFormat dateAndTimeToLeave,
                 String carID, String email) {
        this.registeredCustomerID = registeredCustomerID;
        this.parkingLotID = parkingLotID;
        this.active = active;
        this.dateAndTimeToEnter = dateAndTimeToEnter;
        this.dateAndTimeToLeave = dateAndTimeToLeave;
        this.carID = carID;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public int getRegisteredCustomerID() {
        return registeredCustomerID;
    }

    public void setRegisteredCustomerID(int registeredCustomerID) {
        this.registeredCustomerID = registeredCustomerID;
    }

    public int getParkingLotID() {
        return parkingLotID;
    }

    public void setParkingLotID(int parkingLotID) {
        this.parkingLotID = parkingLotID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public SimpleDateFormat getDateAndTimeToEnter() {
        return dateAndTimeToEnter;
    }

    public void setDateAndTimeToEnter(SimpleDateFormat dateAndTimeToEnter) {
        this.dateAndTimeToEnter = dateAndTimeToEnter;
    }

    public SimpleDateFormat getDateAndTimeToLeave() {
        return dateAndTimeToLeave;
    }

    public void setDateAndTimeToLeave(SimpleDateFormat dateAndTimeToLeave) {
        this.dateAndTimeToLeave = dateAndTimeToLeave;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
