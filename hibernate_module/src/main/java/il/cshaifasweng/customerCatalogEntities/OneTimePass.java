package il.cshaifasweng.customerCatalogEntities;

import il.cshaifasweng.MoneyRelatedServices.Transactions;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class OneTimePass implements Serializable {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private int id;
    @OneToOne(fetch = FetchType.LAZY)
    private Transactions KioskSubOrder;
    @OneToOne(fetch = FetchType.LAZY)
    private ParkingLot parkingLot;
    public OneTimePass(){}
    public OneTimePass(Transactions KioskSubOrder,ParkingLot parkingLot){
        this.KioskSubOrder=KioskSubOrder;
        this.parkingLot=parkingLot;
    }

}
