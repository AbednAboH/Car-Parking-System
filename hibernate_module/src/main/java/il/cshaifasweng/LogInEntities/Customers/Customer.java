package il.cshaifasweng.LogInEntities.Customers;

import il.cshaifasweng.ParkingLotEntities.Car;
import il.cshaifasweng.customerCatalogEntities.Complaint;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy =InheritanceType.JOINED)
@Getter
@Setter
public abstract class Customer implements Serializable {
    @Id
    private long id;
    @Column(name="email")
    private String email;
    @Column(name="firstName")
    private String firstName;
    @Column(name="lastName")
    private String lastName;
    @Column(name="password")
    private String  password;
    @OneToMany(fetch=FetchType.LAZY,mappedBy = "customer" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private List<Car> cars = new ArrayList<>();
    @OneToMany(fetch=FetchType.LAZY,mappedBy = "customer" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private List<Complaint> complaint;
    public Customer() {
    }

    public Customer (int id,String email,String firstName,String lastName,String password){
        this.id=id;
        this.email=email;
        this.firstName=firstName;
        this.lastName=lastName;
        this.password=password;
    }
    public Customer (int id,String email,String firstName,String lastName){
        this.id=id;
        this.email=email;
        this.firstName=firstName;
        this.lastName=lastName;
    }
    public Customer(int id,String email,List<Car> cars) {
        this.id=id;
        this.email=email;
        this.cars = cars;
    }
    public Customer(int id,String email,Car cars) {
        this.id=id;
        this.email=email;
        this.cars.add(cars);
    }
    public Customer(int id,String email,String car) {
        this.id=id;
        this.email=email;
        this.cars.add(new Car(car));
    }
    public void addComplaint(Complaint complaint){
        this.complaint.add(complaint);
    }
}
