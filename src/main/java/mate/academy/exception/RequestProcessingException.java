package mate.academy.exception;

public class RequestProcessingException extends RuntimeException {
    public RequestProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
