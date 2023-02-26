package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class visitorsSubscriberEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public visitorsSubscriberEvent(Message message) {
        this.message = message;
    }

}
