package il.cshaifasweng;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "registered_customer")
public class RegisteredCustomer {

    @OneToMany(fetch= FetchType.LAZY,mappedBy = "Subscription" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private List< Subscription>  Subscriptions;
    @OneToMany(fetch= FetchType.LAZY,mappedBy = "Car" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private List<Car> CarsId;
    @OneToMany(fetch= FetchType.LAZY,mappedBy = "Order" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private  List<Order> Orders;
    @Column(name = "id")
    private String id;
    @Column(name = "meansOfContact")
    private String meansOfContact;

    public RegisteredCustomer(){
    }

    public RegisteredCustomer(List<Subscription> subscriptions,List<String> carsId,List<Orders> orders,  String id, String meansOfContent){
        List<Subscription> Subscription = new ArrayList<>();
        List<String> CarsID = new ArrayList<>();
        List<Order> Orders = new ArrayList<>();
        this.id=id;
        this.meansOfContact =meansOfContent

    }

    public List<Subscription> getSubscriptions() { return Subscriptions;}

    public void setSubscriptions(List<Subscription> subscriptions) { Subscriptions = subscriptions;}

    public List<String> getCarsId() { return CarsId; }

    public void setCarsId(List<String> carsId) { CarsId = carsId;}

    public List<Orders> getOrders() { return Orders; }

    public void setOrders(List<Orders> orders) { Orders = orders;}

    public String getId() { return id;}

    public void setId(String id) { id = id;}

    public String getMeansOfContact() { return meansOfContact; }

    public void setMeansOfContact(String meansOfContact) { this.meansOfContact = meansOfContact;}\

}