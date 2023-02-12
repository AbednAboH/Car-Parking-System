package il.cshaifasweng.MoneyRelatedServices;


import il.cshaifasweng.LogInEntities.Customers.Customer;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Data
@Table(name = "Penalties")
public class Penalty implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "on_late_arrival")
    private double onLateArriaval;
    @Column(name = "on_late_exit")
    private double onLateExit;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Penalty(double onLateArriaval, double onLateExit, Customer customer) {
        this.onLateArriaval = onLateArriaval;
        this.onLateExit = onLateExit;
        this.customer = customer;
    }

    public Penalty() {

    }
}

