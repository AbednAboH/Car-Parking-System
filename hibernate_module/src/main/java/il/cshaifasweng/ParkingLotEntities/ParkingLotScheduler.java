package il.cshaifasweng.ParkingLotEntities;

import il.cshaifasweng.ParkingLotEntities.Vehicle;
import il.cshaifasweng.converters.PriorityQueueConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

@Setter
@Getter
@Entity
@Table(name = "parkingLotScheduler")
public class ParkingLotScheduler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Convert(converter = PriorityQueueConverter.class)
    private PriorityQueue<Vehicle> queue = new PriorityQueue<>(new VehicleComparator());
    @Column(name = "maxCapacity")
    private int maxCapacity;
    @JoinColumn(name = "parkingLot_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ParkingLot parkingLot;
    public void addVehicle(Vehicle vehicle) {
        queue.offer(vehicle);
    }

    public Vehicle getNextVehicle() {
        return queue.poll();
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

