package il.cshaifasweng.ParkingLotEntities;


public enum ConstantMessegesForClient {


    FULL_PARKING_LOT("Parking lot is full"),
    ALREADY_IN_PARKING_LOT("Your vehicle license plate is already in the parking lot"),;




         public String type;


    ConstantMessegesForClient(String type) {
            this.type = type;
        }




}
