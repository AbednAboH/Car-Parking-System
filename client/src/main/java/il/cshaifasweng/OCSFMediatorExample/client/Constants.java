package il.cshaifasweng.OCSFMediatorExample.client;

public enum Constants {
    INVALID_INPUT("Invalid input"),
    NOT_FOUND("Not found"),
    CONNECTION_ERROR("Connection error"),
    INTERNAL_ERROR("An error has occured with our system, please try again or ask for support"),
    REGULAR_SUBSCRIPTION("Regular subscription"),
    REGULAR_MULTI_SUBSCRIPITON("Regular two cars"),
    FULL_SUBSCRIPTION("Full subscription"),

    ;

    private String message;

    Constants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
