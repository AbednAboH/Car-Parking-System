package il.cshaifasweng.customerCatalogEntities;

import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
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
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="registeredCustomer_id")
    private RegisteredCustomer registeredCustomer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parkingLot_id")
    private ParkingLot parkingLotID;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="parkingSpot_id")
//    private ParkingLot parkingSpotID;

    @Column(name="active")
    private boolean active;

    @Column(name="orderDate")
    private LocalDate date;

    @Column(name="enteringTime")
    private String entering;

    @Column(name="exitingTime")
    private String exiting;

    @Column(name="plateNumber")
    private String plateNum;

    @Column(name="email")
    private String email;

    public Order(RegisteredCustomer registeredCustomer, ParkingLot parkingLotID, LocalDate date,
                 String entering, String exiting, String plateNum, String email) {
        this.registeredCustomer = registeredCustomer;
        this.parkingLotID = parkingLotID;
        this.date = date;
        this.entering = entering;
        this.exiting = exiting;
        this.plateNum = plateNum;
        this.email = email;
        this.active = true;
    }
    public Order(RegisteredCustomer registeredCustomer, ParkingLot parkingLotID, LocalDate date,
                 String entering, String exiting, String plateNum, String email,boolean localBuilder) {
        this.registeredCustomer = registeredCustomer;
        this.parkingLotID = parkingLotID;
        this.date = date;
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
        this.parkingLotID = parkingLotID;
        this.date = date;
        this.entering = entering;
        this.exiting = exiting;
        this.plateNum = plateNum;
        this.email = email;
        this.active = true;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="reports_id")
//    private Reports reports;
    public Order() {

    }
    // TODO: 1/10/2023 toString Function 
}
