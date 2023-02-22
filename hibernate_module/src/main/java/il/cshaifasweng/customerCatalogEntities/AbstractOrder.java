package il.cshaifasweng.customerCatalogEntities;

import il.cshaifasweng.MoneyRelatedServices.Transactions;
import il.cshaifasweng.ParkingLotEntities.Car;
import il.cshaifasweng.ParkingLotEntities.EntryAndExitLog;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
public abstract class AbstractOrder extends Transactions {
    @OneToOne
    @JoinColumn(name="EntryAndExitLog_id")
    protected EntryAndExitLog entryAndExitLog;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parkingLot_id")
    protected ParkingLot parkingLotID;
    @Column(name="active")
    protected boolean active;
    @Column(name="exitingTime")
    protected LocalDateTime exiting;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    protected Car car;
    @Column(name="email")
    protected String email;
}
