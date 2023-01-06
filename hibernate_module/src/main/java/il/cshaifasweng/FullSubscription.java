package il.cshaifasweng;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "full_subscriptions")
public class FullSubscription extends Subscription implements Serializable {

    @Column(name = "consecutive_parking_days", nullable = false)
    private int consecutiveParkingDays;

    public FullSubscription(Customer customer, int hoursPerMonth, LocalDate startDate, LocalDate expirationDate, boolean isActive, String allowedDays) {
        super(customer, hoursPerMonth, startDate, expirationDate, isActive, allowedDays);
    }

    public FullSubscription(Customer customer, int hoursPerMonth, LocalDate startDate, LocalDate expirationDate, boolean isActive, String allowedDays, int consecutiveParkingDays) {
        super(customer, hoursPerMonth, startDate, expirationDate, isActive, allowedDays);
        this.consecutiveParkingDays = consecutiveParkingDays;
    }


    public boolean isValid(){
        return getCarsList().size() == 1;
    }

}
