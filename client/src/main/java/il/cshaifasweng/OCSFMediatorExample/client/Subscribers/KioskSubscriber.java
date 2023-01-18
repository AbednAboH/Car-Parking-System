package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class KioskSubscriber {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public KioskSubscriber(Message message) {
        this.message = message;
    }
}
