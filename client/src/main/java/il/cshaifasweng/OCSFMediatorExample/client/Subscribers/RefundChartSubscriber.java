package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class RefundChartSubscriber {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public RefundChartSubscriber(Message message) {
        this.message = message;
    }

}
