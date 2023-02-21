package il.cshaifasweng.customerCatalogEntities;

import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.LogInEntities.Customers.OneTimeCustomer;
import il.cshaifasweng.ParkingLotEntities.Car;
import il.cshaifasweng.ParkingLotEntities.EntryAndExitLog;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table
public class OfflineOrder extends AbstractOrder {
    @ManyToOne
    @JoinColumn
    private Customer customer;

    public EntryAndExitLog getEntryAndExitLog(String licensePlate){
        return entryAndExitLog;
    }
    public EntryAndExitLog getEntryAndExitLog(){
        return entryAndExitLog;
    }

    public OfflineOrder(OneTimeCustomer customer, ParkingLot parkingLotID, String exiting, String car, String email) {
        this.customer = customer;
        this.date=LocalDate.now();
        this.parkingLotID = parkingLotID;
        this.exiting =date.atTime(Integer.parseInt(exiting),0);
        this.car =new Car(car);
        this.email = email;
        this.active = true;
        this.car.setCustomer(this.customer);
        this.car.setTransaction(this);
        this.transactionStatus=false;

    }
    public OfflineOrder(){

    }
}

