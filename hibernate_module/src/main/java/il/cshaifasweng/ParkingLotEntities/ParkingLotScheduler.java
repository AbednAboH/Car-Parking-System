package il.cshaifasweng.ParkingLotEntities;


import il.cshaifasweng.MoneyRelatedServices.Transactions;
import il.cshaifasweng.customerCatalogEntities.OfflineOrder;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import il.cshaifasweng.customerCatalogEntities.Subscription;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import static il.cshaifasweng.ParkingLotEntities.ConstantVariables.*;


/*********  ParkingLotScheduler
 *  This class is responsible for the scheduling of the vehicles in the parking lot
 * It contains a list of vehicles and a priority queue of vehicles
 * The list is used to save the vehicles in the database
 * The priority queue is used to sort the vehicles by priority
 * This class should be used only in the server ! thus uses the data manipulation class as it doesn't have a valid session if its used outside of the server

 **********/
@Setter
@Getter
@Entity
@Table(name = "parkingLotScheduler")
@Inheritance(strategy = InheritanceType.JOINED)
public class ParkingLotScheduler implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    // TODO: 11/02/2023 check if one to many may cause problems
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    protected List<EntryAndExitLog> entryAndExitLogList =new ArrayList<>();
    @Transient
    protected PriorityQueue<EntryAndExitLog> queue=new PriorityQueue<>(new VehicleComparator());

    @Column(name = "maxCapacity")
    protected int maxCapacity;

    public ParkingLotScheduler() {

    }
    public ParkingLotScheduler(ParkingLot parkingLot) {
        this.maxCapacity = parkingLot.getFloor() * parkingLot.getRowsInEachFloor() * parkingLot.getRowCapacity();
        this.entryAndExitLogList = new ArrayList<>();
        this.queue = new PriorityQueue<>(new VehicleComparator());
    }

    public void addToQueue(Transactions orderSubKioskEntities,String car) {
        EntryAndExitLog entryAndExitLog = new EntryAndExitLog(orderSubKioskEntities,this,car);
        queue.offer(entryAndExitLog);
        entryAndExitLogList.add(entryAndExitLog);
    }

    public EntryAndExitLog extractAndLog(Transactions transaction, String licensePlate) {
        if (queue.size()>0) {
            EntryAndExitLog entryAndExitLog= getLogBasedOnType(transaction, licensePlate);
            if( queue.remove(entryAndExitLog)) {
                LocalDateTime exitTime = LocalDateTime.now();
                entryAndExitLogList.remove(entryAndExitLog);
                entryAndExitLog.updateCar(false);
                entryAndExitLog.setAcutallExitTime(exitTime);
                setLogsBasedOnType(transaction, licensePlate, entryAndExitLog);
                return entryAndExitLog;
            }
            else throw new IllegalArgumentException("Vehicle number"+licensePlate+" not found in parking Lot");
        }
        else return null;

    }

    public EntryAndExitLog EnterAndLog(Transactions orderSubKioskEntities, String licensePlate) {
        EntryAndExitLog entryAndExitLog = new EntryAndExitLog(orderSubKioskEntities,this,licensePlate);
        // TODO: 12/02/2023 maybe add -1 to make room for the robot to move ,and check if there are scenarios where the robot can't move
        //  i actually doubt that the robot can't move based on the current requirements
        if (queue.size() <= maxCapacity) {
            LocalDateTime entryTime = LocalDateTime.now();
            entryAndExitLog.updateCar(true);
            entryAndExitLog.setAcutallEntryTime(entryTime);
            queue.offer(entryAndExitLog);
            entryAndExitLogList.add(entryAndExitLog);
            return entryAndExitLog;
        }
        else throw new IllegalArgumentException("Parking Lot is full");
        //todo: throw exception and handle it in the server
    }
    public EntryAndExitLog removeFromQueue() {
        EntryAndExitLog entryAndExitLog = queue.poll();
        entryAndExitLogList.remove(entryAndExitLog);
        return entryAndExitLog;
    }
    public void restoreQueueFromList() {
        // TODO: 11/02/2023 check if the list is sorted
            queue = new PriorityQueue<>(new VehicleComparator());
            for (EntryAndExitLog entryAndExitLog : entryAndExitLogList) {
                queue.offer(entryAndExitLog);

        }

    }


    private static EntryAndExitLog getLogBasedOnType(Transactions transaction, String licensePlate) {
        EntryAndExitLog entryAndExitLog;
        if (FULL_SUBSCRIPTION.isSubscription(transaction))
            entryAndExitLog= ((Subscription) transaction).getEntryAndExitLog(licensePlate);
        else if(ORDER.isOrder(transaction))
            entryAndExitLog= ((OnlineOrder) transaction).getEntryAndExitLog(licensePlate);
        else if(KioskBuyer.isKioskBuyer(transaction)){
            entryAndExitLog= ((OfflineOrder) transaction).getEntryAndExitLog(licensePlate);
        } else
            throw new IllegalArgumentException("Transaction is not a subscription or an order or a kiosk buyer");
        return entryAndExitLog;
    }
    private static void setLogsBasedOnType(Transactions transaction, String licensePlate, EntryAndExitLog entryAndExitLog) {

        if (FULL_SUBSCRIPTION.isSubscription(transaction))
            ((Subscription) transaction).setEntryAndExitLog(licensePlate,entryAndExitLog);
        else if(ORDER.isOrder(transaction))
            ((OnlineOrder) transaction).setEntryAndExitLog(entryAndExitLog);
        else if(KioskBuyer.isKioskBuyer(transaction)){
            ((OfflineOrder) transaction).setEntryAndExitLog(entryAndExitLog);
            //TODO: Kiosk Order Not implemented
            // TODO entryAndExitLog= ((BasicOrder)transaction).setEntryAndExitLog( entryAndExitLog);
        }

    }
    static class VehicleComparator implements Comparator<EntryAndExitLog>,Serializable{
        @Override
        public int compare(EntryAndExitLog v1, EntryAndExitLog v2) {
            int result = Integer.compare(v1.getPriority(), v2.getPriority());
            if (result == 0) {
                result = v1.getEstimatedExitTime().compareTo(v2.getEstimatedExitTime());
            }
            return result;
        }
    }



}

