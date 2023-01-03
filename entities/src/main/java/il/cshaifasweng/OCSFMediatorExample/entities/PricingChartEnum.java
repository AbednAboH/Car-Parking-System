package il.cshaifasweng.OCSFMediatorExample.entities;

public enum PricingChartEnum {
//
    PARK_VIA_KIOSK(8),
    ONE_TIME_PURCHASED_AHEAD(7),
    REGULAR_SUBSCRIPTION(60* ONE_TIME_PURCHASED_AHEAD.value),
    REGULAR_SUBSCRIPTION_MULIPLE_CARS(54 * ONE_TIME_PURCHASED_AHEAD.value),
    FULL_SUBSCRIPTION(72 * ONE_TIME_PURCHASED_AHEAD.value),
    ;

    public final double value;
    private PricingChartEnum(double value){this.value = value;}
}

/*
 @Column(name = "park_via_kiosk")
    private double parkViaKiosk;
    @Column(name = "one_time_purchased_ahead")
    private double oneTimePurchasedAhead;
    @Column(name = "regular_subscription")
    private double regularSubscription;
    @Column(name = "regular_multiple_cars")
    private double regularMultipleCars;
    @Column(name = "full_subscription")
    private double fullSubscription;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    public PricingChart(double parkViaKiosk, double oneTimePurchasedAhead, double regularSubscription, double regularMultipleCars, double fullSubscription) {
        this.parkViaKiosk = parkViaKiosk;
        this.oneTimePurchasedAhead = oneTimePurchasedAhead;
        this.regularSubscription = regularSubscription;
        this.regularMultipleCars = regularMultipleCars;
        this.fullSubscription = fullSubscription;
    }

    public PricingChart() {
    }

    public double getParkViaKiosk() {
        return parkViaKiosk;
    }

    public void setParkViaKiosk(double parkViaKiosk) {
        this.parkViaKiosk = parkViaKiosk;
    }

    //Hourly price
    public double getOneTimePurchasedAhead() {
        return oneTimePurchasedAhead;
    }

    public void setOneTimePurchasedAhead(double oneTimePurchasedAhead) {
        this.oneTimePurchasedAhead = oneTimePurchasedAhead;
    }

    public double getRegularSubscription() {
        return regularSubscription * this.oneTimePurchasedAhead;
    }

    public void setRegularSubscription(double regularSubscription) {
        this.regularSubscription = regularSubscription;
    }

    public double getRegularMultipleCars() {
        return regularMultipleCars * this.oneTimePurchasedAhead;
    }

    public void setRegularMultipleCars(double regularMultipleCars) {
        this.regularMultipleCars = regularMultipleCars;
    }

    public double getFullSubscription() {
        return fullSubscription * this.oneTimePurchasedAhead;
    }

    public void setFullSubscription(double fullSubscription) {
        this.fullSubscription = fullSubscription;
    }
    public double getRegularByNumberOFCars(int numberOfCars){
        return numberOfCars * regularMultipleCars;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    } */
