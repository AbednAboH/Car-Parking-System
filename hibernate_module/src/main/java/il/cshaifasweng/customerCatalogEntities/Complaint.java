package il.cshaifasweng.customerCatalogEntities;

import il.cshaifasweng.LocalDateAttributeConverter;
import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

// TODO: 1/9/2023 check the date format annotation as it may cause trouble in the table entries ,for now its set to Transient
@Entity
@Table(name="complaints")
public class Complaint implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="ComplaintDescription")
    private String complaintDescription;
    @Column(name="TypeOfOrderSub")
    private String typeOfOrderSub;
    @Column(name="orderSubID",nullable = true)
    private String orderSubKioskID;
    @Column(name="Text")
    private String text;
    @Convert(converter = LocalDateAttributeConverter.class)
    @Column (name="DateOfSubmission")
    private LocalDate date;
    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name="remainingTimeToAnswer")
    private LocalDate durationToAnswer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id")
    private Customer customer;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parkingLot_id")
    private ParkingLot parkingLot;
    @Column(name="Active")
    private boolean active;


    public Complaint() {
    }

    public Complaint( String complaintDescription, String text, LocalDate date, LocalDate durationToAnswer, Customer customer, boolean active) {

        this.complaintDescription = complaintDescription;
        this.text = text;
        this.date = date;
        this.durationToAnswer = durationToAnswer;
        this.customer = customer;
        this.active = active;
    }
    public Complaint( String complaintDescription, String text, LocalDate date, LocalDate durationToAnswer, Customer customer, boolean active, ParkingLot pl) {

        this.complaintDescription = complaintDescription;
        this.text = text;
        this.date = date;
        this.durationToAnswer = durationToAnswer;
        this.customer = customer;
        this.active = active;
        parkingLot=pl;
    }

    public Complaint(String complaintDescription, String typeOfOrderSub, String orderSubKioskID, String text, LocalDate date, LocalDate durationToAnswer, boolean active) {
        this.complaintDescription = complaintDescription;
        this.typeOfOrderSub = typeOfOrderSub;
        this.orderSubKioskID = orderSubKioskID;
        this.text = text;
        this.date = date;
        this.durationToAnswer = durationToAnswer;
        this.active = active;
    }

    @Override
    public String toString(){
     return "id="+id+", date="+date;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDurationToAnswer() {
        return durationToAnswer;
    }

    public void setDurationToAnswer(LocalDate durationToAnswer) {
        this.durationToAnswer = durationToAnswer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
}
