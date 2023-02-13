package il.cshaifasweng.ParkingLotEntities;


import il.cshaifasweng.DataManipulationThroughDB.DataBaseManipulation;
import il.cshaifasweng.MoneyRelatedServices.Transactions;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;


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
    private long id;
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
    @Transient
    DataBaseManipulation<ParkingLotScheduler> ParkingLotSchedulerDB = new DataBaseManipulation<>();

    public EntryAndExitLog getVehicleFromDB(Transactions orderSubKioskEntities) {
        String sqlQuery= "SELECT * FROM Vehicle WHERE orderSubKioskEntities_id = " + orderSubKioskEntities.getId();
        return getParkingLotSchedulerDB().queiryData(EntryAndExitLog.class, sqlQuery, new HashMap<String,Object>());

    }
    public EntryAndExitLog exitParkingLot(EntryAndExitLog entryAndExitLog) {
        if (!queue.isEmpty()) {
            if( queue.remove(entryAndExitLog)) {
                LocalDateTime exitTime = LocalDateTime.now();
                entryAndExitLogList.remove(entryAndExitLog);
                entryAndExitLog.updateCar(false);
                entryAndExitLog.setAcutallExitTime(exitTime);
                return entryAndExitLog;
            }
            else throw new IllegalArgumentException("Vehicle not found in parking Lot");
        }
        else return null;

    }
    public EntryAndExitLog enterParkingLot(Transactions orderSubKioskEntities, String licensePlate) {
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
        else return null;
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



    static class VehicleComparator implements Comparator<EntryAndExitLog> {
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

