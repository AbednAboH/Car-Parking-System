package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class CompliantsSubscriber {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public CompliantsSubscriber(Message message) {
        this.message = message;
    }
}