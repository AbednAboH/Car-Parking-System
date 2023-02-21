package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class LogsSubscriber {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public LogsSubscriber(Message message) {
        this.message = message;
    }

}
