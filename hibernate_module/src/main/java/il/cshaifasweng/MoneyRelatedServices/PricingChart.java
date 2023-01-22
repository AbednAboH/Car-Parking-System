package il.cshaifasweng.MoneyRelatedServices;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PricingChart")
@Getter
@Setter
public class PricingChart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "Kiosk")
    private double kioskPrice = 8;
    @Column(name = "OrderBeforehand")
    private double orderBeforeHandPrice = 7;
    @Column(name = "RegularSubscription")
    private double regularSubHours = 60;
    @Column(name = "MultipleCarRegularSubscription")
    private double multipleCarRegularSubHours = 54;
    @Column(name = "FullSubscription")
    private double fullSubHours = 72;
    @Column(name = "SubscriptionsMultiplier")
    private boolean orderBeforeHandAsRate = true;
    @Column(name = "WaitForPermission")
    private boolean WaitForPermission = true;
    @Column(name = "Approved")
    private boolean Approved = false;


    public PricingChart() {

    }


    public PricingChart(double kioskPrice, double orderBeforeHandPrice, double regularSubHours, double multipleCarRegularSubHours, double fullSubHours) {
        this.kioskPrice = kioskPrice;
        this.orderBeforeHandPrice = orderBeforeHandPrice;
        this.regularSubHours = regularSubHours;
        this.multipleCarRegularSubHours = multipleCarRegularSubHours;
        this.fullSubHours = fullSubHours;
    }
    public PricingChart(double kioskPrice, double orderBeforeHandPrice, double regularSubHours, double multipleCarRegularSubHours, double fullSubHours,boolean active) {
        this.kioskPrice = kioskPrice;
        this.orderBeforeHandPrice = orderBeforeHandPrice;
        this.regularSubHours = regularSubHours;
        this.multipleCarRegularSubHours = multipleCarRegularSubHours;
        this.fullSubHours = fullSubHours;
        if (active){
            Approved=true;
            WaitForPermission=false;
        }
    }

    public PricingChart(double kioskPrice, double orderBeforeHandPrice,
                        double regularSubHours, double multipleCarRegularSubHours,
                        double fullSubHours, boolean orderBeforeHand, boolean waitForPermission,
                        boolean approved) {
        this.kioskPrice = kioskPrice;
        this.orderBeforeHandPrice = orderBeforeHandPrice;
        this.regularSubHours = regularSubHours;
        this.multipleCarRegularSubHours = multipleCarRegularSubHours;
        this.fullSubHours = fullSubHours;
        this.orderBeforeHandAsRate = orderBeforeHand;
        WaitForPermission = waitForPermission;
        Approved = approved;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        double subs = kioskPrice;
        if (this.orderBeforeHandAsRate) {
            subs = orderBeforeHandPrice;
        }
        return "Id=" + id + ", kiosk=" + kioskPrice + ", OnlineParchase=" + orderBeforeHandPrice +
                "Subscriptions" + regularSubHours * subs + "," + multipleCarRegularSubHours * subs + "," + fullSubHours * subs;
    }


//    public void addParkingLot(ParkingLot pl){
//        this.parkingLot.add(pl);
//        pl.setPricingChart(this);
//    }
//    public List<ParkingLot> getParkingLot() {
//        return parkingLot;
//    }
//
//    public void setParkingLot(List<ParkingLot> parkingLot) {
//        this.parkingLot = parkingLot;
//    }
}
