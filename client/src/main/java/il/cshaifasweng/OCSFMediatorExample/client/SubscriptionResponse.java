package il.cshaifasweng.OCSFMediatorExample.client;

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
