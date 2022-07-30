package solobob.solobobmate.controller.exception;

public class SoloBobException extends RuntimeException{

    public SoloBobException() {
        super();
    }

    public SoloBobException(String message) {
        super(message);
    }

    public SoloBobException(String message, Throwable cause) {
        super(message, cause);
    }

    public SoloBobException(Throwable cause) {
        super(cause);
    }
}
