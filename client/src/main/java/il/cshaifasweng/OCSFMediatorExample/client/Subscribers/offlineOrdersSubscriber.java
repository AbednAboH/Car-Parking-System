package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class offlineOrdersSubscriber {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public offlineOrdersSubscriber(Message message) {
        this.message = message;
    }

}
