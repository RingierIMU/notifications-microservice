package exceptions;

public class FcmMessageNotSentException extends Exception {
    public FcmMessageNotSentException(String message) {
        super(message);
    }
}
