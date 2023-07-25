package project.bookmark.exception;

public class InValidRefreshException extends RuntimeException{

    public InValidRefreshException() {
        super();
    }
    public InValidRefreshException(String message) {
        super(message);
    }

    public InValidRefreshException(String message, Throwable cause) {
        super(message, cause);
    }

    public InValidRefreshException(Throwable cause) {
        super(cause);
    }

    protected InValidRefreshException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
