package il.cshaifasweng.LogInEntities.Customers;

import il.cshaifasweng.ParkingLotEntities.Car;
import il.cshaifasweng.customerCatalogEntities.Order;
import il.cshaifasweng.customerCatalogEntities.Subscription;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "registered_customer")
@Getter
@Setter
public class RegisteredCustomer extends Customer {

//    @OneToMany(fetch= FetchType.LAZY,mappedBy = "registeredCustomer" ,cascade =CascadeType.ALL,orphanRemoval = true)
//    private  List<Order> orders;
    @OneToMany(fetch= FetchType.LAZY,mappedBy = "registeredCustomer" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private List<Subscription>  Subscriptions;
    public RegisteredCustomer(){
    }


    public RegisteredCustomer(int id, String email, List<Car> cars, List<Subscription> subscriptions) {
        super(id, email, cars);
        Subscriptions = subscriptions;
    }
    public RegisteredCustomer(int id, String email, Car cars) {
        super(id, email, cars);
        Subscriptions=new ArrayList<>() ;

    }
    public RegisteredCustomer(int id, String email, String cars) {
        super(id, email, cars);
        Subscriptions=new ArrayList<>() ;

    }
}