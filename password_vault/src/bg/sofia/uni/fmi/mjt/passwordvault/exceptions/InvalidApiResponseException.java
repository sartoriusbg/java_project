package bg.sofia.uni.fmi.mjt.passwordvault.exceptions;

public class InvalidApiResponseException extends RuntimeException {
    private static final String MESSAGE = "Incorrect API response for password";

    public InvalidApiResponseException() {
        super(MESSAGE);
    }
}
