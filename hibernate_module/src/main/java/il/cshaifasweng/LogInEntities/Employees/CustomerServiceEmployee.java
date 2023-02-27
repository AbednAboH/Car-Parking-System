package il.cshaifasweng.LogInEntities.Employees;

import il.cshaifasweng.customerCatalogEntities.Complaint;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name="CostumerServiceEmployee")
public class CustomerServiceEmployee extends Employee {
    @OneToMany(fetch = FetchType.LAZY)
    private List<ParkingLot> parkingLot;
    @OneToMany(fetch=FetchType.LAZY)
    private List<Complaint> complaints;

    public CustomerServiceEmployee(String firstName, String lastName, String title, String email, double salary, List<ParkingLot> parkingLot, List<Complaint> complaints) {
        super(firstName, lastName, title, email, salary);
        this.parkingLot = parkingLot;
        this.complaints = complaints;
    }
    public CustomerServiceEmployee(String firstName, String lastName, String title, String email, double salary) {
        super(firstName, lastName, title, email, salary);

    }

    public CustomerServiceEmployee() {

    }
    @Override
    public String getGUI() {
        return "CompliantsListScreen";
    }


    public List<ParkingLot> getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(List<ParkingLot> parkingLot) {
        this.parkingLot = parkingLot;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }


}






