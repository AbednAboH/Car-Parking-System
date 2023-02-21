package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class CustomerRefundsSubscriber {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public CustomerRefundsSubscriber(Message message) {
        this.message = message;
    }

}
