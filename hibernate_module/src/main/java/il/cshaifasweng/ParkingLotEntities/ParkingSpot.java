package il.cshaifasweng.ParkingLotEntities;


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
    private int id;
    @Column(name="rowNum")
    private int row;
    @Column(name="floor")
    private int floor;
    @Column(name="depth")
    private int depth;
    @Column(name="Occupied")
    private boolean occupied;
    @JoinColumn(name="entryAndExitLog_id")
    @OneToOne(fetch=FetchType.LAZY,cascade ={CascadeType.MERGE})
    private EntryAndExitLog entryAndExitLog;
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

    public void setEntryAndExitLog(EntryAndExitLog entryAndExitLog) {
        this.entryAndExitLog = entryAndExitLog;
        this.entryAndExitLog.setParkingSpot(this);
        occupied=true;
    }
    public void resetEntryAndExitLog() {
        if (entryAndExitLog!=null&&entryAndExitLog.getParkingSpot()!=null)
            entryAndExitLog.setParkingSpot(null);
        entryAndExitLog = null;
        occupied=false;
    }
    public EntryAndExitLog removeVehicle(){
        EntryAndExitLog v = this.entryAndExitLog;
        this.entryAndExitLog = null;
        occupied=false;
        return v;
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

    public boolean ChangeFaultyStatus(){
        if(isOccupied())
            return false;
        //TODO: save a spot in another parking lot
        if(isSaved())
        {
            setSaved(false);
        }
        setFaulty(!isFaulty());
        return true;
    }

    public boolean ChangeSavedStatus(){
        if(isOccupied() || isFaulty() || isSaved())
            return false;
        //TODO: if notifying the customer is needed
        if(isSaved())
        {
            setSaved(false);
            return true;
        }
        setSaved(true);
        return true;
    }

    public int getId() {
        return id;
    }
}
