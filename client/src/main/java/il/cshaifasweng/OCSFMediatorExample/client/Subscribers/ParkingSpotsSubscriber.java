package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class ParkingSpotsSubscriber {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public ParkingSpotsSubscriber(Message message) {
        this.message = message;
    }

}
