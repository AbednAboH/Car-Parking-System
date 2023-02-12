package il.cshaifasweng.ParkingLotEntities;


import il.cshaifasweng.DataManipulationThroughDB.DataBaseManipulation;
import il.cshaifasweng.MoneyRelatedServices.Transactions;
import lombok.*;
import org.hibernate.Session;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;


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
    protected List<Vehicle> vehicleList=new ArrayList<>();
    @Transient
    protected PriorityQueue<Vehicle> queue=new PriorityQueue<>(new VehicleComparator());

    @Column(name = "maxCapacity")
    protected int maxCapacity;

    public ParkingLotScheduler() {

    }
    public ParkingLotScheduler(ParkingLot parkingLot) {
        this.maxCapacity = parkingLot.getFloor() * parkingLot.getRowsInEachFloor() * parkingLot.getRowCapacity();
        this.vehicleList = new ArrayList<>();
        this.queue = new PriorityQueue<>(new VehicleComparator());
    }

    public void addToQueue(Transactions orderSubKioskEntities,String car) {
        Vehicle vehicle = new Vehicle(orderSubKioskEntities,this,car);
        queue.offer(vehicle);
        vehicleList .add(vehicle);
    }
    @Transient
    DataBaseManipulation<ParkingLotScheduler> ParkingLotSchedulerDB = new DataBaseManipulation<>();

    public Vehicle getVehicleFromDB(Transactions orderSubKioskEntities) {
        String sqlQuery= "SELECT * FROM Vehicle WHERE orderSubKioskEntities_id = " + orderSubKioskEntities.getId();
        return getParkingLotSchedulerDB().queiryData(Vehicle.class, sqlQuery, new HashMap<String,Object>());

    }
    public Vehicle exitParkingLot(Vehicle vehicle) {
        if (!queue.isEmpty()) {
            if( queue.remove(vehicle)) {
                vehicleList.remove(vehicle);
                ParkingLotSchedulerDB.update(this);
                return vehicle;
            }
            else throw new IllegalArgumentException("Vehicle not found in parking Lot");
        }
        else return null;

    }
    public Vehicle enterParkingLot(Transactions orderSubKioskEntities,String licensePlate) {
        Vehicle vehicle = new Vehicle(orderSubKioskEntities,this,licensePlate);
        // TODO: 12/02/2023 maybe add -1 to make room for the robot to move ,and check if there are scenarios where the robot can't move
        //  i actually doubt that the robot can't move based on the current requirements
        if (queue.size() < maxCapacity) {
            vehicle.updateCar();
            queue.offer(vehicle);
            vehicleList.add(vehicle);


            return vehicle;
        }
        else return null;

    }
    public Vehicle removeFromQueue() {
        Vehicle vehicle = queue.poll();
        vehicleList.remove(vehicle);
        return vehicle;
    }
    public void restoreQueueFromList() {
        // TODO: 11/02/2023 check if the list is sorted
        if (queue.isEmpty()){
            queue = new PriorityQueue<>(new VehicleComparator());
            for (Vehicle vehicle : vehicleList) {
                queue.offer(vehicle);
            }
        }

    }



    static class VehicleComparator implements Comparator<Vehicle> {
        @Override
        public int compare(Vehicle v1, Vehicle v2) {
            int result = Integer.compare(v1.getPriority(), v2.getPriority());
            if (result == 0) {
                result = v1.getEstimatedExitTime().compareTo(v2.getEstimatedExitTime());
            }
            return result;
        }
    }


}

