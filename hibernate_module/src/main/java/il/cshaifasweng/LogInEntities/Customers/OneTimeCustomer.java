package il.cshaifasweng.LogInEntities.Customers;

import il.cshaifasweng.MoneyRelatedServices.Refund;
import il.cshaifasweng.ParkingLotEntities.Car;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Table(name = "one_time_customer")
public class OneTimeCustomer extends Customer {

    @OneToMany(fetch= FetchType.LAZY,cascade =CascadeType.ALL,orphanRemoval = true)
    private List<Refund> refunds;
    public void addRefund(Refund refund){
        refunds.add(refund);
    }
    public OneTimeCustomer(){
    }

    public OneTimeCustomer(int id, String email, Car cars, LocalTime exitTime) {
        super(id, email, cars);

    }
    public OneTimeCustomer(int id,String email,String firstName,String lastName,String password) {
        super(id,email,firstName,lastName, password);
    }
}