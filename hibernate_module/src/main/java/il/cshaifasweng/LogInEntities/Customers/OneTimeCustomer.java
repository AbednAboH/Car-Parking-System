package il.cshaifasweng.LogInEntities.Customers;

import il.cshaifasweng.ParkingLotEntities.Car;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Table(name = "one_time_customer")
public class OneTimeCustomer extends Customer {

    @Column(name = "exit_time")
    private LocalTime exitTime;

    public OneTimeCustomer(){
    }

    public OneTimeCustomer(int id, String email, Car cars, LocalTime exitTime) {
        super(id, email, cars);
        this.exitTime = exitTime;
    }
}