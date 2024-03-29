package il.cshaifasweng.LogInEntities.Customers;

import il.cshaifasweng.MoneyRelatedServices.Refund;
import il.cshaifasweng.ParkingLotEntities.Car;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
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
    @OneToMany(fetch= FetchType.LAZY,cascade =CascadeType.ALL,orphanRemoval = true)
    private List<Refund> refunds;
    @OneToMany(fetch= FetchType.LAZY,mappedBy = "registeredCustomer" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private  List<OnlineOrder> OnlineOrders=new ArrayList<>();
    @OneToMany(fetch= FetchType.LAZY,mappedBy = "registeredCustomer" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private List<Subscription> subscriptions;
    public void addRefund(Refund refund){
        refunds.add(refund);
    }
    @Override
    public String getGUI(){
        return "newCustomerPage";
    }
    public boolean isCustomerByDefinition(){
        return subscriptions.size()>0;
    }
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

    public void addOrder(OnlineOrder newOnlineOrder){
        OnlineOrders.add(newOnlineOrder);

        if (!this.getCars().contains(newOnlineOrder.getCar())){
            newOnlineOrder.getCar().setCustomer(this);
            this.getCars().add(newOnlineOrder.getCar());
        }
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