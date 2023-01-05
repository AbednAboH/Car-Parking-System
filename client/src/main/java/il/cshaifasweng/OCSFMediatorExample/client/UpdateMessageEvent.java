package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;

public class UpdateMessageEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public UpdateMessageEvent(Message message) {
        this.message = message;
    }
}
