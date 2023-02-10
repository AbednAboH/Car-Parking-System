package il.cshaifasweng.LogInEntities.Employees;

import il.cshaifasweng.ParkingLotEntities.ParkingLot;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="globalManagers")
public class GlobalManager extends Employee {
    @OneToMany(fetch = FetchType.LAZY)
    private List<ParkingLot> parkingLot;


    public GlobalManager(String name, String title, double salary) {
        super(name, title, salary);
    }
    public GlobalManager(){}
}
