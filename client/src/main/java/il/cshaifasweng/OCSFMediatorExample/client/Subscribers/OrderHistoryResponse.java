package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class OrderHistoryResponse {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public OrderHistoryResponse(Message message) {
        this.message = message;
    }
}
