package il.cshaifasweng;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "registered_customer")
public class RegisteredCustomer extends Customer{


    @OneToMany(fetch= FetchType.LAZY,mappedBy = "customer" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private List< Subscription>  Subscriptions;
    @OneToMany(fetch= FetchType.LAZY,mappedBy = "customer" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private  List<Order> Orders;
    @Column(name = "means_of_contact")
    private String meansOfContact;
    public RegisteredCustomer(){
    }

    public RegisteredCustomer(List<Subscription> subscriptions, List<Order> orders, String meansOfContact) {
        Subscriptions = subscriptions;
        Orders = orders;
        this.meansOfContact = meansOfContact;
    }

    public RegisteredCustomer(List<Subscription> subscriptions, List<Order> orders) {
        Subscriptions = subscriptions;
        Orders = orders;
    }

    public RegisteredCustomer(List<Car> cars, List<Subscription> subscriptions, List<Order> orders, String meansOfContact) {
        super(cars);
        Subscriptions = subscriptions;
        Orders = orders;
        this.meansOfContact = meansOfContact;
    }
//    We may need to add more constructors accordingly

}