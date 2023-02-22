package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class UnconfirmedArrivalSubscriber {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public UnconfirmedArrivalSubscriber(Message message) {
        this.message = message;
    }

}
