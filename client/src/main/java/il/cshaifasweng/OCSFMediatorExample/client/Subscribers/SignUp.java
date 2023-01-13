package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class SignUp {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public SignUp(Message message) {
        this.message = message;
    }
}