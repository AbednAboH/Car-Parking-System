package il.cshaifasweng;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="parkingLotManagers")
public class ParkingLotManager extends Employee{
    @OneToOne(fetch = FetchType.LAZY)
    private ParkingLot parkingLot;

    public ParkingLotManager(String name, String title, double salary) {
        super(name, title, salary);
    }

    public ParkingLotManager() {
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }
    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
    @Override
    public String toString(){
        return super.toString() +  ", parkingLot=" + parkingLot ;
    }
}
