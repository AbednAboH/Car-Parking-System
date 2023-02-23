package il.cshaifasweng.OCSFMediatorExample.server;


import il.cshaifasweng.Message;

public enum ServerMessegesEnum {

    EMPTY("Test,TEst"),
    LOGIN("#LogIn"),

    REGISTER("#Register"),
    INITIALIZE_PARKING_LOT("#intializeParkingLot"),
    GET_ALL_PARKING_LOTS("#getAllParkingLots"),


    NEW_ORDER("#placeOrder"),

    GET_USER("#getUser"),
    GET_PRICING_CHART("#getPricingChart"),
    UPDATE_PRICING_CHART("#updatePrice"),
    UPDATE_SUBSCRIPTION_IN_CHART("#updateAmount"),
    DIRECT_TO_AVAILABLE_PARK("#DirectToAvailblePark"),
    GET_PARKING_SPOTS("#GetParkingSpots"),
    SET_PARKING_SPOTS("#SetParkingSpots"),
    GET_ORDERS("#showOrders"),
    GET_SUBSCRIPTIONS("#showSubscription"),
    NEW_SUBSCRIPTION("#addSubscription"),
    CANCEL_ORDER("#cancelOrder"),
    CANCEL_SUBSCRIPTION("#cancelSubscription"),
    GET_REFUND_CHART("#GetRefundChart"),
    CONNECTION_ALIVE("#ConnectionAlive"),
    GET_ALL_ORDERS("#getAllOrders"),
    APPLY_COMPLAINT("#applyComplaint"),
    GET_ALL_COMPLAINTS("#GetAllCompliants"),
    CLOSE_COMPLAINT("#CloseComplaint"),
    VERIFY_SUBSCRIPTION("#verifySubscription"),
    VERIFY_ORDER("#verifyOrder"),
    GET_CUSTOMER_CARS("#GetCustomerCars"),
    CANCEL_ORDER_AND_GET_REFUND("#CancelOrderAndGetRefund"),
    LOGOUT("#LogOut"),
    ENTER_PARKING_LOT("#EnterParkingLot"),
    EXIT_PARKING_LOT("#ExitParkingLot"),
    GET_CUSTOMER_REFUNDS("#getRefunds"),
    GET_CUSTOMER_TRANSACTIONS("#getTransactions"),
    GET_CUSTOMER_LOGS("#getEntryAndExitLogs"),
    GET_CUSTOMER_OFFLINE_ORDERS("#getOfflineOrders"),
    GET_ORDERS_REQUIRING_Confirmation("#getToBeConfirmed"),
    CONFIRM_CUSTOMER_ARRIVAL("#confirmCustomerArrival"),
    GET_ACTIVE_ORDERS("#GetActiveOrders"),
    GET_ALL_ORDERS_FOR_MANAGER("#GetAllOrdersForManager"),
    REJECT_ALL_PRICE_REQUEST("#RejectAllPriceRequests"),
    GET_ONLINE_ORDER("#getOnlineOrder"),
    ;

    final String type;

    ServerMessegesEnum(String type) {
        this.type = type;
    }
    int startswith(String message){
        return message.startsWith(type)?ordinal():-1;
    }
}
