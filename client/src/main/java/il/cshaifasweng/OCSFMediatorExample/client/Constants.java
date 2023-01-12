package il.cshaifasweng.OCSFMediatorExample.client;

public enum Constants {
    INVALID_INPUT("Invalid input"),
    NOT_FOUND("Not found"),
    CONNECTION_ERROR("Connection error"),
    INTERNAL_ERROR("An error has occured with our system, please try again or ask for support"),
    ;

    private String message;

    Constants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
