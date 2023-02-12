package il.cshaifasweng.ParkingLotEntities;


import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parkingspots")
@Getter
@Setter
public class ParkingSpot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="rowNum")
    private int row;
    @Column(name="floor")
    private int floor;
    @Column(name="depth")
    private int depth;
    @Column(name="Occupied")
    private boolean occupied;
    @JoinColumn(name="vehicle_id")
    @OneToOne(fetch=FetchType.LAZY,cascade =CascadeType.ALL,orphanRemoval = true)
    private Vehicle vehicle;
    @Column(name="SavedSpace")
    private boolean saved;
    @Column(name="faulty")
    private boolean faulty;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parkinglots_id")
    private ParkingLot parkingLot;

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }
    public ParkingSpot(int row, int floor, int depth,ParkingLot parkingLot) {
        this.row = row;
        this.floor = floor;
        this.depth = depth;
        occupied=false;

        saved=false;
        setParkingLot(parkingLot);
    }

    public ParkingSpot(int row, int floor, int depth, boolean occupied, String currentCarId, boolean saved,ParkingLot parkingLot) {
        this.row = row;
        this.floor = floor;
        this.depth = depth;
        this.occupied = occupied;
        this.saved = saved;
        setParkingLot(parkingLot);
    }

    public ParkingSpot() {

    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        occupied=true;
    }
    public Vehicle removeVehicle(){
        Vehicle v = this.vehicle;
        this.vehicle = null;
        occupied=false;
        return v;
    }
    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    // TODO: 12/02/2023 check this one , don't like it one bit !
    public boolean free(){
        if(this.isOccupied())
            return false;
        if(this.isSaved())
            this.saved = false;
        return true;
    }
    public boolean markAsSaved(){
        if(this.isOccupied())
            return false;
        if(!this.isSaved())
            this.saved = true;
        return true;
    }
    @Override
    public String toString(){
        return "parkingLotId="+parkingLot.getId()+" ["+floor+"]"+"["+row+"]"+"["+depth+"]= OC"+occupied+" S"+saved;
    }


    public long getId() {
        return id;
    }
}
