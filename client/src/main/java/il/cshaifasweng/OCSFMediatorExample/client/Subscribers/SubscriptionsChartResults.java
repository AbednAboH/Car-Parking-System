package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class SubscriptionsChartResults {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public SubscriptionsChartResults(Message message) {
        this.message = message;
    }
}
