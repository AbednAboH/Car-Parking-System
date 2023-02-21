package il.cshaifasweng.ParkingLotEntities;

import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.MoneyRelatedServices.Transactions;
import il.cshaifasweng.customerCatalogEntities.*;
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
    private int id;
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
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Customer customer;
    @OneToOne
    @JoinColumn(name = "parking_spot_id")
     private ParkingSpot parkingSpot;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parking_lot_scheduler_id")
    private ParkingLotScheduler parkingLotScheduler;
     public EntryAndExitLog(Transactions orderSubKioskEntity, ParkingLotScheduler parkingLotScheduler, String activeCar) {
        this.parkingLotScheduler = parkingLotScheduler;
        RegularSubscription rSub;
        FullSubscription fSub;
        OnlineOrder onlineOrder;
        OfflineOrder offlineOrder;
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
            this.customer=rSub.getRegisteredCustomer();
        }
        else if(identifyEntranceIdentity.startsWith(FULL_SUBSCRIPTION.type)){
            fSub = (FullSubscription) orderSubKioskEntity;
            int remaining=fSub.getHoursPerMonth();
            LocalDateTime now=LocalDateTime.now();
            this.estimatedExitTime =  now.plusHours(remaining);
            this.priority = FULL_SUBSCRIPTION.priority;
            this.customer=fSub.getRegisteredCustomer();
            fSub.getCar(this.activeCar).setActiveCar(true);

        }
        else if(identifyEntranceIdentity.equals(ORDER.type)){
            onlineOrder =(OnlineOrder) orderSubKioskEntity;
            this.estimatedExitTime = onlineOrder.getDateOfOrder().plusHours(onlineOrder.getHoursOfResidency());
            this.priority = ORDER.priority;
            this.customer=onlineOrder.getRegisteredCustomer();
            onlineOrder.getCar().setActiveCar(true);
        }
        else if(identifyEntranceIdentity.equals(KioskBuyer.type)){
            // TODO: 10/02/2023 to be defined
            this.priority = KioskBuyer.priority;
            offlineOrder = (OfflineOrder) orderSubKioskEntity;
            this.estimatedExitTime= offlineOrder.getExiting();
            offlineOrder.getCar().setActiveCar(true);
            this.customer=offlineOrder.getCustomer();

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
            ((OnlineOrder)orderSubKioskEntity).getCar().setActiveCar(active);
            if(active)
                ((OnlineOrder)orderSubKioskEntity).setEntryAndExitLog(this);
            else
                ((OnlineOrder)orderSubKioskEntity).setActive(false);


        }
        else if (ORDER.isKioskBuyer( orderSubKioskEntity)){

            // TODO: 12/02/2023 to be defined same line as below
             ((OfflineOrder)orderSubKioskEntity).getCar().setActiveCar(active);
            if(active)
                ((OfflineOrder)orderSubKioskEntity).setEntryAndExitLog(this);
            else
                ((OfflineOrder)orderSubKioskEntity).setActive(false);
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
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof EntryAndExitLog)) {
            return false;
        }

        EntryAndExitLog otherLog = (EntryAndExitLog) obj;

        return this.activeCar.equals(otherLog.activeCar) && this.orderSubKioskEntity.equals(otherLog.orderSubKioskEntity) &&
                this.parkingLotScheduler.equals(otherLog.parkingLotScheduler) && this.acutallEntryTime.equals(otherLog.acutallEntryTime)
                && this.acutallExitTime.equals(otherLog.acutallExitTime) && this.estimatedExitTime.equals(otherLog.estimatedExitTime) &&
                this.priority == otherLog.priority&& this.activeLog == otherLog.activeLog;
    }
}