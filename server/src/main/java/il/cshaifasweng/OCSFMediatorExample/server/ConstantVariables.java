package il.cshaifasweng.OCSFMediatorExample.server;


import il.cshaifasweng.customerCatalogEntities.FullSubscription;
import il.cshaifasweng.customerCatalogEntities.Order;
import il.cshaifasweng.customerCatalogEntities.RegularSubscription;

public enum ConstantVariables {

    CONNECTION_ERROR("Connection error"),
    INTERNAL_ERROR("An error has occured with our system, please try again or ask for support"),
    REGULAR_SUBSCRIPTION(RegularSubscription.class.getSimpleName()),
    REGULAR_MULTI_SUBSCRIPITON(RegularSubscription.class.getSimpleName() + "TwoCars"),
    FULL_SUBSCRIPTION(FullSubscription.class.getSimpleName()),
    ORDER(Order.class.getSimpleName()),
    KioskBuyer("KioskBuyer"),
    ;

    private String message;

    ConstantVariables(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
