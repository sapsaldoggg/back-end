package solobob.solobobmate.controller.exception;

public class DropTheCodeException extends RuntimeException{

    public DropTheCodeException() {
        super();
    }

    public DropTheCodeException(String message) {
        super(message);
    }

    public DropTheCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DropTheCodeException(Throwable cause) {
        super(cause);
    }
}
