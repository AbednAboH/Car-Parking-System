package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class SubscriptionResponse {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public SubscriptionResponse(Message message) {
        this.message = message;
    }
}
