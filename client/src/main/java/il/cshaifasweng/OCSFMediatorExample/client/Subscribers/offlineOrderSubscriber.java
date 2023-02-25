package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class offlineOrderSubscriber {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public offlineOrderSubscriber(Message message) {
        this.message = message;
    }
}
