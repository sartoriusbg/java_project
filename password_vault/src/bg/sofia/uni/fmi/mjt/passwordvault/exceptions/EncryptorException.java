package bg.sofia.uni.fmi.mjt.passwordvault.exceptions;

public class EncryptorException extends RuntimeException {
    private static final String ENCRYPTOR_PROBLEM_MESSAGE = "Problem with encryption/decryption";

    public EncryptorException() {
        super(ENCRYPTOR_PROBLEM_MESSAGE);
    }
}
