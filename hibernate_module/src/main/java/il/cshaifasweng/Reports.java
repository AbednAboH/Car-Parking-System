package il.cshaifasweng;


import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="reports")
public class Reports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Complaint> complaints;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Order> OrdersHistory;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ParkingSpot> unavailableParkingSpots;
    @OneToOne()
    @JoinColumn(name="parkingLot_id")
    private ParkingLot parkingLot;



}
