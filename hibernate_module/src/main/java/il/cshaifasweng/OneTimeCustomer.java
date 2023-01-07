package il.cshaifasweng;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Table(name = "one_time_customer")
public class OneTimeCustomer extends Customer{

    @Column(name = "exit_time")
    private LocalTime exitTime;

    public OneTimeCustomer(){
    }

    public OneTimeCustomer(LocalTime exitTime) {
        this.exitTime = exitTime;
    }

    public OneTimeCustomer(List<Car> customers, LocalTime exitTime) {
        super(customers);
        this.exitTime = exitTime;
    }
}