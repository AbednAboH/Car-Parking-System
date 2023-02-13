package il.cshaifasweng.ParkingLotEntities;

import il.cshaifasweng.DataManipulationThroughDB.DataBaseManipulation;
import il.cshaifasweng.MoneyRelatedServices.Transactions;
import il.cshaifasweng.customerCatalogEntities.FullSubscription;
import il.cshaifasweng.customerCatalogEntities.Order;
import il.cshaifasweng.customerCatalogEntities.RegularSubscription;
import il.cshaifasweng.customerCatalogEntities.Subscription;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static il.cshaifasweng.ParkingLotEntities.ConstantVariables.*;
@Entity
@Table(name = "EntryAndExitLog")
@Getter
@Setter
public class EntryAndExitLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    // TODO: 12/02/2023 should be changed to functions rather than variables
    @Column(name = "priority")
    private int priority;
    @Column(name = "estimated_exit_time")
    private LocalDateTime estimatedExitTime;
    @Column(name="activeLog")
    boolean activeLog=false;
    @Column(name="activeCar")
    String activeCar;
    @Column(name="acutallEntryTime")
    private LocalDateTime acutallEntryTime;
    @Column(name="acutallExitTime")
    private LocalDateTime acutallExitTime;
    @ManyToOne( cascade = CascadeType.ALL)
    private Transactions orderSubKioskEntity;
    @OneToOne
    @JoinColumn(name = "parking_spot_id")
     private ParkingSpot parkingSpot;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parking_lot_scheduler_id")
    private ParkingLotScheduler parkingLotScheduler;
    @Transient
    DataBaseManipulation<EntryAndExitLog> vehicleDB = new DataBaseManipulation<>();
    public EntryAndExitLog(Transactions orderSubKioskEntity, ParkingLotScheduler parkingLotScheduler, String activeCar) {
        this.parkingLotScheduler = parkingLotScheduler;
        RegularSubscription rSub;
        FullSubscription fSub;
        Order order;
        this.activeCar=activeCar;
        String identifyEntranceIdentity = orderSubKioskEntity.getClass().getSimpleName();
        if (identifyEntranceIdentity.startsWith(REGULAR_SUBSCRIPTION.type)){
            rSub = (RegularSubscription) orderSubKioskEntity;
            this.estimatedExitTime =  FromLocalTimeToDateTime(rSub.getExtractionDate());
            if (identifyEntranceIdentity.equals(REGULAR_SUBSCRIPTION.type)){
                this.priority = REGULAR_SUBSCRIPTION.priority;
            }
            else
                this.priority = REGULAR_MULTI_SUBSCRIPITON.priority;
            rSub.getCar(this.activeCar).setActiveCar(true);
        }
        else if(identifyEntranceIdentity.startsWith(FULL_SUBSCRIPTION.type)){
            fSub = (FullSubscription) orderSubKioskEntity;
            long remaining=fSub.getHoursPerMonth();
            LocalDateTime now=LocalDateTime.now();
            this.estimatedExitTime =  now.plusHours(remaining);
            this.priority = FULL_SUBSCRIPTION.priority;
            fSub.getCar(this.activeCar).setActiveCar(true);

        }
        else if(identifyEntranceIdentity.equals(ORDER.type)){
            order=(Order) orderSubKioskEntity;
            this.estimatedExitTime = order.getDateOfOrder().plusHours(order.getHoursOfResidency());
            this.priority = ORDER.priority;
            order.getCar().setActiveCar(true);
        }
        else if(identifyEntranceIdentity.equals(KioskBuyer.type)){
            // TODO: 10/02/2023 to be defined
            this.priority = KioskBuyer.priority;

        }
        else
            System.out.println("Error Unable To Identify Entrance Identity");
        this.orderSubKioskEntity = orderSubKioskEntity;
        this.activeCar=activeCar;

    }

    public EntryAndExitLog() {

    }

    public void updateCar(boolean active) {
        activeLog=active;
        if (FULL_SUBSCRIPTION.isSubscription(orderSubKioskEntity)){
            ((Subscription)orderSubKioskEntity).getCar(activeCar).setActiveCar(active);
            if (active)
                ((Subscription)orderSubKioskEntity).setEntryAndExitLogs(this);

        }
        else if (ORDER.isOrder(orderSubKioskEntity)){
            ((Order)orderSubKioskEntity).getCar().setActiveCar(active);
            if(active)
                ((Order)orderSubKioskEntity).setEntryAndExitLog(this);

        }
        else if (ORDER.isKioskBuyer( orderSubKioskEntity)){

            // TODO: 12/02/2023 to be defined same line as below
//            if(active)
//                ((Order)orderSubKioskEntity).setEntryAndExitLog(this);
        }
        else
            System.out.println("Error Unable To Identify Entrance Identity");


    }
    private LocalDateTime FromLocalTimeToDateTime(LocalTime time){
        LocalDateTime dateTime=LocalDateTime.now();
        dateTime=dateTime.withHour(time.getHour());
        dateTime=dateTime.withMinute(time.getMinute());
        dateTime=dateTime.withSecond(time.getSecond());
        return dateTime;
    }
    public int getPriority() {
        return priority;
    }

    public LocalDateTime getEstimatedExitTime() {
        return estimatedExitTime;
    }
}