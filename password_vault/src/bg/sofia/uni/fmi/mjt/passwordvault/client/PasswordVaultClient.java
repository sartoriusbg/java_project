package bg.sofia.uni.fmi.mjt.passwordvault.client;

import bg.sofia.uni.fmi.mjt.passwordvault.errorhandler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.passwordvault.exceptions.ServerConnectionProblemException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class PasswordVaultClient {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;
    private static final String DISCONNECT_COMMAND = "disconnect";
    private static final String LOGOUT_COMMAND = "logout";
    private static final String LOGIN_SUCCESSFUL = "Successful login";
    private static final String NOT_LOGGED_PROFILE = "null";
    private static final String CONNECTION_MESSAGE = "Connected to the PasswordVault server.";
    private static final String CONNECTION_ERROR_MESSAGE = "There is a problem with the network communication";
    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public static void main(String[] args) {
        String profile = NOT_LOGGED_PROFILE;

        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            System.out.println(CONNECTION_MESSAGE);

            while (true) {
                String message = scanner.nextLine();

                if (DISCONNECT_COMMAND.equals(message)) {
                    break;
                }
                if (LOGOUT_COMMAND.equals(message)) {
                    profile = NOT_LOGGED_PROFILE;
                }
                message = new StringBuilder().append(message).append(" ").append(profile).toString();
                buffer.clear();
                buffer.put(message.getBytes());
                buffer.flip();
                socketChannel.write(buffer);

                buffer.clear();
                socketChannel.read(buffer);
                buffer.flip();

                byte[] byteArray = new byte[buffer.remaining()];
                buffer.get(byteArray);
                String reply = new String(byteArray, "UTF-8");

                if (reply.equals(LOGIN_SUCCESSFUL)) {
                    profile = message.split(" ")[1];
                }
                System.out.println(reply);
            }

        } catch (IOException e) {
            System.out.println(CONNECTION_ERROR_MESSAGE);
            ServerConnectionProblemException ex = new ServerConnectionProblemException();
            ErrorHandler.writeLogs(ex);
        }
    }
}
