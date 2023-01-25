package il.cshaifasweng.LogInEntities.Customers;

import il.cshaifasweng.MoneyRelatedServices.Refund;
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

    @OneToMany(fetch= FetchType.LAZY,mappedBy = "registeredCustomer" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private  List<Order> orders;
    @OneToMany(fetch= FetchType.LAZY,mappedBy = "registeredCustomer" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private List<Subscription> subscriptions;
    @OneToMany(fetch= FetchType.LAZY,mappedBy = "registeredCustomer" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private List<Refund> refunds;
    public RegisteredCustomer(){

    }
    public RegisteredCustomer(int id,String email,String name,String lastName,String password){
        super(id,email,name,lastName,password);
    }
    public RegisteredCustomer(int id,String email,String name,String lastName){
        super(id,email,name,lastName);
    }
    public RegisteredCustomer(int id, String email, List<Car> cars, List<Subscription> subscriptions) {
        super(id, email, cars);
        this.subscriptions = subscriptions;
    }
    public RegisteredCustomer(int id, String email, Car cars) {
        super(id, email, cars);
        subscriptions =new ArrayList<>() ;

    }
    public RegisteredCustomer(int id, String email, String cars) {
        super(id, email, cars);
        subscriptions =new ArrayList<>() ;

    }

    public void addOrder(Order newOrder){
        orders.add(newOrder);
        Car car=new Car(newOrder.getPlateNum());
        if (!this.getCars().contains(car))
            this.getCars().add(car);
    }
    public void addSubscription(Subscription sub){
        subscriptions.add(sub);
        AddCar(sub.getCarsList());
    }
    public void AddCar(List<Car> cars){
        for (Car car:
             cars) {if (!this.getCars().contains(car))
                this.getCars().add(car);
        }
    }
}