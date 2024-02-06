package assertionbit.trainapi.messages;

import org.springframework.stereotype.Component;

public class ErrorResponse
{
    protected String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
