package bg.sofia.uni.fmi.mjt.passwordvault.server;

import bg.sofia.uni.fmi.mjt.passwordvault.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.passwordvault.restapi.SecurePasswordApiClient;
import bg.sofia.uni.fmi.mjt.passwordvault.storage.PasswordStorage;

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.util.Scanner;

public class ServerStarter {
    private static final Path PROFILES_FILE = Path.of("profiles.txt");
    private static final int PORT = 7777;

    public static void start() {
        Scanner in = new Scanner(System.in);
        Runnable server;
        if (PROFILES_FILE.toFile().exists()) {
            server = new PasswordVaultServer(PORT, new CommandExecutor(new PasswordStorage(PROFILES_FILE),
                    new SecurePasswordApiClient(HttpClient.newBuilder().build())));
        } else {
            server = new PasswordVaultServer(PORT, new CommandExecutor(new PasswordStorage(),
                    new SecurePasswordApiClient(HttpClient.newBuilder().build())));
        }
        Thread serverThread = new Thread(server);

        serverThread.start();

        while (!in.nextLine().trim().equalsIgnoreCase("stop")) {
        }

        PasswordVaultServer.stop();

        in.close();
    }
}
