package il.cshaifasweng.customerCatalogEntities;

import il.cshaifasweng.LocalDateAttributeConverter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order implements Serializable {
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

    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name = "reservationDate")
    private LocalDate reservationDate;

    @Column(name = "enter")
    private String enteringHour;

    @Column(name = "exit")
    private String exitingHour;

    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name = "enteringTimeAndDate")
    private LocalDate dateAndTimeToEnter;

    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name = "leavingTimeAndDate")
    private LocalDate dateAndTimeToLeave;

    @Column(name = "carID")
    private String carID;

    @Column(name = "email")
    private String email;

    public Order() {

    }

    public Order(int registeredCustomerID, int parkingLotID, boolean active,
                 LocalDate dateAndTimeToEnter, LocalDate dateAndTimeToLeave,
                 String carID, String email) {
        this.registeredCustomerID = registeredCustomerID;
        this.parkingLotID = parkingLotID;
        this.active = active;
        this.dateAndTimeToEnter = dateAndTimeToEnter;
        this.dateAndTimeToLeave = dateAndTimeToLeave;
        this.carID = carID;
        this.email = email;
    }


}
