package bg.sofia.uni.fmi.mjt.passwordvault.errorhandler;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;



import static org.junit.jupiter.api.Assertions.assertTrue;

public class ErrorHandlerTest {

    private final Path SERVER_ERRORS_LOGS = Path.of("ServerLogs.txt");
    private final Path CUSTOM_ERRORS_LOGS = Path.of("CustomErrorLogsTest.txt");

    @Test
    void testWriteLogs_withDefaultFilePath() {
        File file = SERVER_ERRORS_LOGS.toFile();
        if (file.exists()) {
            file.delete();
        }
        Exception exception = new Exception("Test Exception");
        ErrorHandler.writeLogs(exception);
        assertTrue(file.exists(), "The file should exist");
        file.delete();
    }

    @Test
    void testWriteLogs_withCustomFilePath() {
        File file = CUSTOM_ERRORS_LOGS.toFile();
        if (file.exists()) {
            file.delete();
        }
        Exception exception = new Exception("Test Exception");
        ErrorHandler.writeLogs(exception, CUSTOM_ERRORS_LOGS);
        assertTrue(file.exists(), "The file should exist");
        file.delete();
    }


}
