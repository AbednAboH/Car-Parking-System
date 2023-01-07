package il.cshaifasweng;


import net.bytebuddy.implementation.ToStringMethod;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parkingspots")
public class ParkingSpot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="rowNum")
    private int rrow;
    @Column(name="floor")
    private int floor;
    @Column(name="depth")
    private int depth;
    @Column(name="Occupied")
    private boolean occupied;
    @Column(name="CarID")
    private String currentCarID;
    @Column(name="SavedSpace")
    private boolean saved;

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
        this.rrow = row;
        this.floor = floor;
        this.depth = depth;
        occupied=false;
        currentCarID ="";
        saved=false;
        setParkingLot(parkingLot);
    }

    public ParkingSpot(int row, int floor, int depth, boolean occupied, String currentCarId, boolean saved,ParkingLot parkingLot) {
        this.rrow = row;
        this.floor = floor;
        this.depth = depth;
        this.occupied = occupied;
        this.currentCarID = currentCarId;
        this.saved = saved;
        setParkingLot(parkingLot);
    }

    public ParkingSpot() {

    }

    public int getRow() {
        return rrow;
    }

    public void setRow(int row) {
        this.rrow = row;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int column) {
        this.floor = column;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String getCurrentCarID() {
        return currentCarID;
    }

    public void setCurrentCarID(String currentCarId) {
        this.currentCarID = currentCarId;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

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
        return "parkingLotId="+parkingLot.getId()+" ["+floor+"]"+"["+rrow+"]"+"["+depth+"]= OC"+occupied+" S"+saved;
    }


}
