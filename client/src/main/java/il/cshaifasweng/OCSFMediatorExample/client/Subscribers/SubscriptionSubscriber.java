package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class SubscriptionSubscriber {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public SubscriptionSubscriber(Message message) {
        this.message = message;
    }

}
