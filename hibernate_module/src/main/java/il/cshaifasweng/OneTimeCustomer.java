package il.cshaifasweng;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "one_time_customer")
public class OneTimeCustomer {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="customerId")
    private String customerId;
    @Column(name = "carId")
    private String carId;
    @Column(name = "exitTime")
    private LocalTime exitTime;

    public OneTimeCustomer(){
    }
    public OneTimeCustomer(String customerId , String carId , LocalTime exitTime){
        this.carId=carId;
        this.customerId=customerId;
        this.exitTime=exitTime;
    }

    public String getCarId() { return carId; }

    public void setCarId(String carId) { this.carId = carId; }

    public String getCustomerId() { return customerId; }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalTime getExitTime() { return exitTime;}

    public void setExitTime(LocalTime exitTime) { this.exitTime = exitTime;}
}