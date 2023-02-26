package il.cshaifasweng.LogInEntities.Employees;

import il.cshaifasweng.ParkingLotEntities.ParkingLot;

import javax.persistence.*;

@Entity
@Table(name="parkingLotManagers")
public class ParkingLotManager extends Employee {
    @OneToOne(fetch = FetchType.LAZY)
    private ParkingLot parkingLot;

    public ParkingLotManager(String name, String title, double salary) {
        super(name, title, salary);
    }
    public ParkingLotManager(String firstname,String lastname,String title,String email,double salary){
        super(firstname,lastname,title,email,salary);
    }
    public ParkingLotManager() {
    }

    @Override
    public String getGUI() {
        return "ParkingLotManager";
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }
    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
    @Override
    public String toString(){
        return "parkingLotManager"+super.toString() +  ", parkingLot=" + parkingLot.getId() ;
    }
}
