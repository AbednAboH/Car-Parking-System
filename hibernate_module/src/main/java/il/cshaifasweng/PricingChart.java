package il.cshaifasweng;

import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.OneToOne;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Table(name = "prices")
public class PricingChart implements Serializable {

    @Column(name = "Order/Subscription Type")
    private final String parkViaKioskName = "Park via Kiosk";
    private final String oneTimePurchaseName = "One Time Purchase";
    private final String regularSubName = "Regular Subscription";
    private final String regularSubWithCarsName = "Regular Subscription with multiple cars";
    private final String fullSubName = "Full Subscription";

    @Column(name = "Order/Subscription Price per Hour")
    private Double parkViaKioskHourly;
    private Double oneTimePurchaseHourly;
    private Double regularSubHourly;
    private Double regularSubWithCarsHourly;
    private Double fullSubHourly;

    @Column(name = "Subscription Hours Monthly")
    private int parkViaKioskMonthlyHours = 0;
    private int oneTimePurchaseMonthlyHours = 0;
    private int regularSubMonthlyHours;
    private int regularSubWithCarsMonthlyHours;
    private int fullSubMonthlyHours;

    @Column(name = "Order/Subscription Total Price")
    private Double parkViaKioskTotal;
    private Double oneTimePurchaseTotal;
    private Double regularSubTotal;
    private Double regularSubWithCarsTotal;
    private Double fullSubTotal;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public PricingChart() {

    }

    public PricingChart(Double parkViaKioskHourly, Double oneTimePurchaseHourly
            , int regularSubMonthlyHours, int regularSubWithCarsMonthlyHours
            , int fullSubMonthlyHours) {
        this.parkViaKioskHourly = parkViaKioskHourly;
        this.oneTimePurchaseHourly = oneTimePurchaseHourly;
        this.regularSubHourly = oneTimePurchaseHourly;
        this.regularSubWithCarsHourly = oneTimePurchaseHourly;
        this.fullSubHourly = oneTimePurchaseHourly;

        this.regularSubMonthlyHours = regularSubMonthlyHours;
        this.regularSubWithCarsMonthlyHours = regularSubWithCarsMonthlyHours;
        this.fullSubMonthlyHours = fullSubMonthlyHours;

        this.parkViaKioskTotal = parkViaKioskHourly;
        this.oneTimePurchaseTotal = oneTimePurchaseHourly;
        this.regularSubTotal = oneTimePurchaseHourly * regularSubMonthlyHours;
        this.regularSubWithCarsTotal = oneTimePurchaseHourly * regularSubWithCarsMonthlyHours;
        this.fullSubTotal = oneTimePurchaseHourly * fullSubMonthlyHours;
    }

    public int getId() {
        return id;
    }

    public String getParkViaKioskName() {
        return parkViaKioskName;
    }

    public String getOneTimePurchaseName() {
        return oneTimePurchaseName;
    }

    public String getRegularSubName() {
        return regularSubName;
    }

    public String getRegularSubWithCarsName() {
        return regularSubWithCarsName;
    }

    public String getFullSubName() {
        return fullSubName;
    }

    public Double getParkViaKioskHourly() {
        return parkViaKioskHourly;
    }

    public void setParkViaKioskHourly(Double parkViaKioskHourly) {
        this.parkViaKioskHourly = parkViaKioskHourly;
        this.parkViaKioskTotal = parkViaKioskHourly;
    }

    public Double getOneTimePurchaseHourly() {
        return oneTimePurchaseHourly;
    }

    public void setOneTimePurchaseHourly(Double oneTimePurchaseHourly) {
        this.oneTimePurchaseHourly = oneTimePurchaseHourly;
        this.regularSubHourly = oneTimePurchaseHourly;
        this.regularSubWithCarsHourly = oneTimePurchaseHourly;
        this.fullSubHourly = oneTimePurchaseHourly;
        this.oneTimePurchaseTotal = oneTimePurchaseHourly;
        this.regularSubTotal = oneTimePurchaseHourly * regularSubMonthlyHours;
        this.regularSubWithCarsTotal = oneTimePurchaseHourly * regularSubWithCarsMonthlyHours;
        this.fullSubTotal = oneTimePurchaseHourly * fullSubMonthlyHours;
    }

    public Double getRegularSubHourly() {
        return regularSubHourly;
    }

    public Double getRegularSubWithCarsHourly() {
        return regularSubWithCarsHourly;
    }

    public Double getFullSubHourly() {
        return fullSubHourly;
    }

    public int getParkViaKioskMonthlyHours() {
        return parkViaKioskMonthlyHours;
    }

    public int getOneTimePurchaseMonthlyHours() {
        return oneTimePurchaseMonthlyHours;
    }

    public int getRegularSubMonthlyHours() {
        return regularSubMonthlyHours;
    }

    public void setRegularSubMonthlyHours(int regularSubMonthlyHours) {
        this.regularSubMonthlyHours = regularSubMonthlyHours;
        this.regularSubTotal = oneTimePurchaseHourly * regularSubMonthlyHours;
        System.out.println(this.regularSubTotal);
    }

    public int getRegularSubWithCarsMonthlyHours() {
        return regularSubWithCarsMonthlyHours;
    }

    public void setRegularSubWithCarsMonthlyHours(int regularSubWithCarsMonthlyHours) {
        this.regularSubWithCarsMonthlyHours = regularSubWithCarsMonthlyHours;
        this.regularSubWithCarsTotal = oneTimePurchaseHourly * regularSubWithCarsMonthlyHours;
    }

    public int getFullSubMonthlyHours() {
        return fullSubMonthlyHours;
    }

    public void setFullSubMonthlyHours(int fullSubMonthlyHours) {
        this.fullSubMonthlyHours = fullSubMonthlyHours;
        this.fullSubTotal = oneTimePurchaseHourly * fullSubMonthlyHours;
    }

    public Double getParkViaKioskTotal() {
        return parkViaKioskTotal;
    }

    public Double getOneTimePurchaseTotal() {
        return oneTimePurchaseTotal;
    }

    public Double getRegularSubTotal() {
        return regularSubTotal;
    }

    public Double getRegularSubWithCarsTotal() {
        return regularSubWithCarsTotal;
    }

    public Double getFullSubTotal() {
        return fullSubTotal;
    }

}
