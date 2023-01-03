package il.cshaifasweng;

import javax.persistence.*;

@Entity
@Table(name="parkingLotEmployees")
public class ParkingLotEmployee extends Employee{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parkingLot_id")
    private ParkingLot parkingLot;

    public ParkingLotEmployee(String name, String title, double salary) {
        super(name, title, salary);
    }
    public ParkingLotEmployee() {}
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
