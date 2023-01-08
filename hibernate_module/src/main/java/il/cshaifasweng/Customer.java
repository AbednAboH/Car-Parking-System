package il.cshaifasweng;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public abstract class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(fetch=FetchType.LAZY,mappedBy = "customer" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private List<Car> cars = new ArrayList<>();

    public Customer() {
    }

    public Customer(List<Car> cars) {
        this.cars = cars;
    }

}
