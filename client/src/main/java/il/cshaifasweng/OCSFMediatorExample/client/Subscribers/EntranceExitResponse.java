package il.cshaifasweng.OCSFMediatorExample.client.Subscribers;

import il.cshaifasweng.Message;

public class EntranceExitResponse {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public EntranceExitResponse(Message message) {
        this.message = message;
    }

}
