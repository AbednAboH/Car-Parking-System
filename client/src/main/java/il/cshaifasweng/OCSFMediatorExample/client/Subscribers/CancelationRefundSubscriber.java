package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class CancelationRefundSubscriber {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public CancelationRefundSubscriber(Message message) {
        this.message = message;
    }

}
