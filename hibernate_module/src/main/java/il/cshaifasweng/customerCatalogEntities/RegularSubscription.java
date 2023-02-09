package il.cshaifasweng.customerCatalogEntities;


import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "regular_subscriptions")
public class RegularSubscription extends Subscription {

    @ManyToOne
    @JoinColumn(name = "parkingLot_id",nullable = false)
    private ParkingLot desegnatedParkingLot;

    @Column(columnDefinition = "TIME",nullable = false)
    private LocalDate extractionDate;
    @Override
    public  String getParkingLotIdAsString(){
        return Integer.toString(desegnatedParkingLot.getId());
    }
    public RegularSubscription(Customer customer, int hoursPerMonth, LocalDate startDate, LocalDate expirationDate, boolean isActive, String allowedDays) {
        super(customer, hoursPerMonth, startDate, expirationDate, isActive, allowedDays);
    }

    public RegularSubscription(Customer customer, int hoursPerMonth, LocalDate startDate, LocalDate expirationDate, boolean isActive, String allowedDays, ParkingLot desegnatedParkingLot, LocalDate extractionDate) {
        super(customer, hoursPerMonth, startDate, expirationDate, isActive, allowedDays);
        this.desegnatedParkingLot = desegnatedParkingLot;
        this.extractionDate = extractionDate;
    }
  public RegularSubscription(int hoursPerMonth, LocalDate startDate, LocalDate expirationDate, boolean isActive, String allowedDays, ParkingLot desegnatedParkingLot, LocalDate extractionDate) {
        super( hoursPerMonth, startDate, expirationDate, isActive, allowedDays);
        this.desegnatedParkingLot = desegnatedParkingLot;
        this.extractionDate = extractionDate;
    }

    public RegularSubscription() {
        super();
    }
    @Override
    public String toString(){
        return super.toString()+ extractionDate+"=+extractionDate";
    }
}
