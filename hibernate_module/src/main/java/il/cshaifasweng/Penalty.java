package il.cshaifasweng;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Data
@Table("Penalties")
public class Penalty implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "onLateArrival")
    private double onLateArriaval;
    @Column(name = "onLateExit")
    private double onLateExit;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "customerID")
    private Customer customer;

    public Penalty(double onLateArriaval, double onLateExit, Customer customer) {
        this.onLateArriaval = onLateArriaval;
        this.onLateExit = onLateExit;
        this.customer = customer;
    }
}

