package model.api.exceptions;

public class IsbnApiException extends RuntimeException {
    public IsbnApiException(final String message) {
        super(message);
    }

    public IsbnApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
