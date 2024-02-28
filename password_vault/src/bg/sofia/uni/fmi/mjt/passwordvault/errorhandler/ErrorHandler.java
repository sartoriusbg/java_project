package bg.sofia.uni.fmi.mjt.passwordvault.errorhandler;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

public class ErrorHandler {
    private static final Path SERVER_ERRORS_LOGS = Path.of("ServerLogs.txt");

    public static void writeLogs(Throwable exception) {
        try (PrintStream ps = new PrintStream(new FileOutputStream(SERVER_ERRORS_LOGS.toFile(), true))) {
            exception.printStackTrace(ps);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeLogs(Throwable exception, Path errorFile) {
        try (PrintStream ps = new PrintStream(new FileOutputStream(errorFile.toFile(), true))) {
            exception.printStackTrace(ps);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
