package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class NewSubscriberEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public NewSubscriberEvent(Message message) {
        this.message = message;
    }
}
