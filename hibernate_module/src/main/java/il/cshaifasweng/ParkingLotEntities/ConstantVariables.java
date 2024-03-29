package il.cshaifasweng.ParkingLotEntities;


import il.cshaifasweng.customerCatalogEntities.OfflineOrder;
import il.cshaifasweng.MoneyRelatedServices.Transactions;
import il.cshaifasweng.customerCatalogEntities.FullSubscription;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import il.cshaifasweng.customerCatalogEntities.RegularSubscription;

public enum ConstantVariables {

    CONNECTION_ERROR("Connection error", 1),
    INTERNAL_ERROR("An error has occured with our system, please try again or ask for support",2),
    REGULAR_SUBSCRIPTION(RegularSubscription.class.getSimpleName(),3),
    REGULAR_MULTI_SUBSCRIPITON(RegularSubscription.class.getSimpleName() + "TwoCars",3),
    FULL_SUBSCRIPTION(FullSubscription.class.getSimpleName(),3),
    ORDER(OnlineOrder.class.getSimpleName(),4),
    KioskBuyer(OfflineOrder.class.getSimpleName(),5);


    String type;
    int priority;

    ConstantVariables(String type,int priority) {
        this.type = type;
        this.priority=priority;
    }
    public boolean isSubscription(Transactions orderSubKioskEntity){
        String identifyEntranceIdentity = orderSubKioskEntity.getClass().getSimpleName();
        return identifyEntranceIdentity.startsWith(REGULAR_SUBSCRIPTION.type)||identifyEntranceIdentity.startsWith(FULL_SUBSCRIPTION.type);
    }
    public boolean isOrder(Transactions orderSubKioskEntity){
        String identifyEntranceIdentity = orderSubKioskEntity.getClass().getSimpleName();
        return identifyEntranceIdentity.startsWith(ORDER.type);
    }
    public boolean isKioskBuyer(Transactions orderSubKioskEntity){
        String identifyEntranceIdentity = orderSubKioskEntity.getClass().getSimpleName();
        return identifyEntranceIdentity.startsWith(KioskBuyer.type);
    }


}
