package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class PriceRequestsSubscriber {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public PriceRequestsSubscriber(Message message) {
        this.message = message;
    }

}
