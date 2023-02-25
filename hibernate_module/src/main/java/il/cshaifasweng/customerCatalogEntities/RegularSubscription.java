package il.cshaifasweng.customerCatalogEntities;


import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "regular_subscriptions")
public class RegularSubscription extends Subscription {

    @ManyToOne
    @JoinColumn(name = "parkingLot_id",nullable = false)
    private ParkingLot desegnatedParkingLot;

    @Column(columnDefinition = "TIME",nullable = false)
    private LocalTime extractionDate;
    @Override
    public  String getParkingLotIdAsString(){
        return Integer.toString(desegnatedParkingLot.getId());
    }


    public RegularSubscription(Customer customer, int hoursPerMonth, LocalDate startDate, LocalDate expirationDate, List<String> cars, int value,
                               String transaction_method, boolean transactionStatus, ParkingLot desegnatedParkingLot, LocalTime extractionDate) {
        super(customer, hoursPerMonth, startDate, expirationDate, true, "1111100",cars,value,transaction_method,transactionStatus);

        this.desegnatedParkingLot = desegnatedParkingLot;
        this.extractionDate = extractionDate;
    }
  public RegularSubscription(Customer customer,int hoursPerMonth, LocalDate startDate, LocalDate expirationDate, ParkingLot desegnatedParkingLot, LocalTime extractionDate, List<String> cars) {
        super( customer,hoursPerMonth, startDate, expirationDate,true, "1111100",cars);
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
