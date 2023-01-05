package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;

public class ErrorEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public ErrorEvent(Message message) {
        this.message = message;
    }
}
