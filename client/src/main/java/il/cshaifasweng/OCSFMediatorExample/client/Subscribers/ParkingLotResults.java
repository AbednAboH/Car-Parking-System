package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class ParkingLotResults {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public ParkingLotResults(Message message) {
        this.message = message;
    }
}
