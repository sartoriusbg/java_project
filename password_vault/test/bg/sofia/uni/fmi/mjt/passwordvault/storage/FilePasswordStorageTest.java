package bg.sofia.uni.fmi.mjt.passwordvault.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FilePasswordStorageTest {
    private static final Path PROFILES_FILE = Path.of("profiles.txt");
    private FilePasswordStorage test;

    @BeforeEach
    void setup() {
        test = new FilePasswordStorage();
    }

    @Test
    void registerTest() {
        test.register("profile", "password", "password");
        assertTrue(PROFILES_FILE.toFile().exists(), "The profiles file should exist");
        assertTrue(Path.of("profilepasswords.txt").toFile().exists(), "The passwords file should exist");
        try (BufferedReader r = new BufferedReader(new FileReader(PROFILES_FILE.toFile()))) {
            assertEquals("profile password", r.readLine(), "Should have the line in the file");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Path.of("profilepasswords.txt").toFile().delete();
        PROFILES_FILE.toFile().delete();
    }

    @Test
    void addPasswordTest() {
        test.register("profile", "password", "password");
        test.addPassword("profile", "website", "user", "password");
        assertTrue(PROFILES_FILE.toFile().exists());
        assertTrue(Path.of("profilepasswords.txt").toFile().exists());
        try(BufferedReader r = new BufferedReader(new FileReader(Path.of("profilepasswords.txt").toFile()))) {
            assertEquals("website user password", r.readLine(), "Should have the line in the file");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Path.of("profilepasswords.txt").toFile().delete();
        PROFILES_FILE.toFile().delete();
    }

    @Test
    void removePasswordTest() {
        test.register("profile", "password", "password");
        test.addPassword("profile", "website", "user", "password");
        assertTrue(test.removePassword("profile", "website", "user"), "Should return true");
        Path.of("profilepasswords.txt").toFile().delete();
        PROFILES_FILE.toFile().delete();
    }

}
