package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class CurrentPriceSubscriber {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public CurrentPriceSubscriber(Message message) {
        this.message = message;
    }
}