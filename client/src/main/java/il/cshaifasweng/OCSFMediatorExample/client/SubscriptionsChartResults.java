package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class SubscriptionsChartResults {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public SubscriptionsChartResults(Message message) {
        this.message = message;
    }
}
