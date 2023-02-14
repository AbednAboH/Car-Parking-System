package il.cshaifasweng.ParkingLotEntities;


import il.cshaifasweng.MoneyRelatedServices.Transactions;
import il.cshaifasweng.customerCatalogEntities.FullSubscription;
import il.cshaifasweng.customerCatalogEntities.Order;
import il.cshaifasweng.customerCatalogEntities.RegularSubscription;

public enum ConstantMessegesForClient {


    FULL_PARKING_LOT("Parking lot is full"),;




         public String type;


    ConstantMessegesForClient(String type) {
            this.type = type;
        }




}
