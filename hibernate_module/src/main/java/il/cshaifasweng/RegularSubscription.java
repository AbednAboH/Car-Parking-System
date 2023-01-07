package il.cshaifasweng;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "regular_subscriptions")
public class RegularSubscription extends Subscription implements Serializable {

    @ManyToOne
    @Column(name = "parking_lot_id",nullable = false)
    private ParkingLot desegnatedParkingLot;

    @Column(columnDefinition = "TIME",nullable = false)
    private LocalDate extractionDate;

    public RegularSubscription(Customer customer, int hoursPerMonth, LocalDate startDate, LocalDate expirationDate, boolean isActive, String allowedDays) {
        super(customer, hoursPerMonth, startDate, expirationDate, isActive, allowedDays);
    }

    public RegularSubscription(Customer customer, int hoursPerMonth, LocalDate startDate, LocalDate expirationDate, boolean isActive, String allowedDays, ParkingLot desegnatedParkingLot, LocalDate extractionDate) {
        super(customer, hoursPerMonth, startDate, expirationDate, isActive, allowedDays);
        this.desegnatedParkingLot = desegnatedParkingLot;
        this.extractionDate = extractionDate;
    }
}
