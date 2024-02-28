package bg.sofia.uni.fmi.mjt.passwordvault.exceptions;

public class ServerConnectionProblemException extends RuntimeException {
    private static final String CONNECTION_ERROR_MESSAGE = "There is a problem with the network communication";

    public ServerConnectionProblemException() {
        super(CONNECTION_ERROR_MESSAGE);
    }
}
