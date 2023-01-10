package il.cshaifasweng.MoneyRelatedServices;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PricingChart")
public class PricingChart implements Serializable {
//    @OneToMany(fetch=FetchType.LAZY,mappedBy = "pricingChart" ,cascade =CascadeType.ALL,orphanRemoval = true)
//    private List<ParkingLot> parkingLot;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="subscriptionOrderName")
    private String name;
    @Column(name="ByHourOrSubscription")
    private boolean byHourOrSubscription;
    @Column(name="parkingRateId")
    private int rateId;
    @Column(name="Rate")
    private double rate;


    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isByHourOrSubscription() {
        return byHourOrSubscription;
    }

    public void setByHourOrSubscription(boolean byHourOrSubscription) {
        this.byHourOrSubscription = byHourOrSubscription;
    }

    public int getRateId() {
        return rateId;
    }

    public void setRateId(int rateId) {
        this.rateId = rateId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public PricingChart() {

    }
    public PricingChart(PricingChart pc) {
        this.id=pc.id;
        this.name=pc.name;
        this.rate=pc.rate;
        this.byHourOrSubscription=pc.byHourOrSubscription;
        this.rateId=pc.rateId;

    }

    public PricingChart(String name,boolean byHourOrSubscription,int rateId,double rate) {
        this.name=name;
        this.byHourOrSubscription=byHourOrSubscription;
        //if by hour:
        this.rate=rate;
        if (byHourOrSubscription){
            this.rateId=id;
        }
        else{this.rateId=rateId;}

    }

    public int getId() {
        return id;
    }
    @Override
    public String toString(){
        return "Id="+id+" ,name="+name+" ,type="+byHourOrSubscription+", rate="+rate+" ,rateId="+rateId;
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
