package bg.sofia.uni.fmi.mjt.passwordvault.exceptions;

public class HashingAlgorithException extends RuntimeException {
    private static final String MESSAGE = "Problem with hashing algorithm";

    public HashingAlgorithException() {
        super(MESSAGE);
    }
}
