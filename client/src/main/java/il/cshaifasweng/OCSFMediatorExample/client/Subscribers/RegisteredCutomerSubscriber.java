package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class RegisteredCutomerSubscriber {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public RegisteredCutomerSubscriber(Message message) {
        this.message = message;
    }
}