package il.cshaifasweng.MoneyRelatedServices;


import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.ParkingLotEntities.ParkingSpot;
import il.cshaifasweng.customerCatalogEntities.Complaint;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="reports")
public class Reports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Complaint> complaints;
//    @OneToMany(fetch = FetchType.LAZY,mappedBy = "reports",cascade = CascadeType.ALL,orphanRemoval = true)
//    private List<Order> orders;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ParkingSpot> unavailableParkingSpots;
    @OneToOne()
    @JoinColumn(name="parkingLot_id")
    private ParkingLot parkingLot;



}
