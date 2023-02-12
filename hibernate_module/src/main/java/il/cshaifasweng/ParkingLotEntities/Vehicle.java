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
@Table(name = "vehicles")
@Getter
@Setter
public class Vehicle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "priority")
    private int priority;
    @Column(name = "estimated_exit_time")
    private LocalDateTime estimatedExitTime;
    @Column(name="activeCar")
    String activeCar;
    @OneToOne
    @JoinColumn(name = "order_sub_kiosk_entity_id")
    private Transactions orderSubKioskEntity;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parking_lot_scheduler_id")
    private ParkingLotScheduler parkingLotScheduler;
    @Transient
    DataBaseManipulation<Vehicle> vehicleDB = new DataBaseManipulation<>();
    public Vehicle( Transactions orderSubKioskEntity, ParkingLotScheduler parkingLotScheduler,String activeCar) {
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

    public Vehicle() {

    }

    public void updateCar() {
        if (FULL_SUBSCRIPTION.isSubscription(orderSubKioskEntity))
            ((Subscription)orderSubKioskEntity).getCar(activeCar).setActiveCar(true);
        else if (ORDER.isOrder(orderSubKioskEntity))
            ((Order)orderSubKioskEntity).getCar().setActiveCar(true);
        else if (ORDER.isKioskBuyer( orderSubKioskEntity)){
            // TODO: 12/02/2023 to be defined
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