package bg.sofia.uni.fmi.mjt.passwordvault.exceptions;

public class ApiConnectionException extends RuntimeException {
    private static final String MESSAGE = "Cant connect to the Api for secure passwords";

    public ApiConnectionException() {
        super(MESSAGE);
    }
}
