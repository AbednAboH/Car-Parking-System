package il.cshaifasweng.LogInEntities.Employees;

import il.cshaifasweng.ParkingLotEntities.ParkingLot;

import javax.persistence.*;

@Entity
@Table(name="parkingLotEmployees")
public class ParkingLotEmployee extends Employee {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parkingLot_id")
    private ParkingLot parkingLot;

    public ParkingLotEmployee(String name, String title, double salary) {
        super(name, title, salary);
    }
    public ParkingLotEmployee(String firstname,String lastname,String title,String email,double salary,ParkingLot parkingLot) {
        super(firstname,lastname,title,email,salary);
        this.parkingLot=parkingLot;
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
        return "ParkingLotEmployee"+super.toString()+"parkingLotId="+parkingLot.getId();
    }
}
