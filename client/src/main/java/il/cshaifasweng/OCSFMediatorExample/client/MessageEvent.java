package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;

public class MessageEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public MessageEvent(Message message) {
        this.message = message;
    }
}
