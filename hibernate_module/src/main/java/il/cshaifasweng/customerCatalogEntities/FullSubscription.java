package il.cshaifasweng.customerCatalogEntities;

import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.ParkingLotEntities.EntryAndExitLog;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "full_subscriptions")
public class FullSubscription extends Subscription {

    @Column(name = "consecutive_parking_days", nullable = false)
    private int consecutiveParkingDays;


    public FullSubscription(Customer customer, int hoursPerMonth, LocalDate startDate, LocalDate expirationDate, List<String> cars, int value,
                            String transaction_method, boolean transactionStatus, int consecutiveParkingDays) {
        super(customer, hoursPerMonth, startDate, expirationDate, true, "1111111",cars,value,transaction_method,transactionStatus);
        this.consecutiveParkingDays = consecutiveParkingDays;
    }

    public FullSubscription(Customer customer, int hoursPerMonth, LocalDate startDate, LocalDate expirationDate, List<String> cars,int consecutiveParkingDays) {
        super( customer,hoursPerMonth, startDate, expirationDate,true, "1111100",cars);
        this.consecutiveParkingDays = consecutiveParkingDays;
    }

    public FullSubscription() {
    }
    public boolean hasParkedConsicutively(){
        int counter = 0;
        List<EntryAndExitLog> logs = this.getEntryAndExitLogs();
        logs=this.getEntryAndExitLogs().stream().filter(entryAndExitLog -> {
            return entryAndExitLog.getAcutallEntryTime().isAfter(LocalDateTime.now().minusDays(consecutiveParkingDays));
        }).toList();
        return logs.size()>=consecutiveParkingDays;

    }
    public boolean hasParkedToday(){
        List<EntryAndExitLog> logs = this.getEntryAndExitLogs();
        logs=this.getEntryAndExitLogs().stream().filter(entryAndExitLog -> {
            return entryAndExitLog.getAcutallEntryTime().toLocalDate().equals(LocalDate.now());
        }).toList();
        return logs.size()>=1;

    }


    public boolean isValid(){
        return getCarsList().size() == 1;
    }

    public String toString(){
        return super.toString()+", consicutiveDays"+consecutiveParkingDays;
    }
}
