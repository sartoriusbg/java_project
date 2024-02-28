package bg.sofia.uni.fmi.mjt.passwordvault.filetomemory;

import bg.sofia.uni.fmi.mjt.passwordvault.storage.WebsiteUserCombination;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class FromFilesTest {
    @BeforeEach
    void setup() {
        try(BufferedWriter w = new BufferedWriter(new FileWriter("profiles.txt", true));
        BufferedWriter wm =new BufferedWriter(new FileWriter("mitkopasswords.txt", true));
        BufferedWriter wp = new BufferedWriter(new FileWriter("peshopasswords.txt", true))) {
            w.write("mitko mpassword" + System.lineSeparator());
            w.flush();
            w.write("pesho ppassword" + System.lineSeparator());
            wm.write("fb dimitar fbpassword");
            wp.write("fb pesho fbppassword");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void destroy() {
        Path.of("profiles.txt").toFile().delete();
        Path.of("mitkopasswords.txt").toFile().delete();
        Path.of("peshopasswords.txt").toFile().delete();
    }

    @Test
    void getProfileMapTest() {
        var test = FromFiles.getProfilesFileMap();
        assertTrue(test.containsKey("mitko") && test.containsKey("pesho"),
                "Should contain keys in the files");
    }

    @Test
    void getProfileMemoryMapTest() {
        var test = FromFiles.getProfileMemoryMap();
        assertTrue(test.containsKey("mitko") && test.containsKey("pesho"),
                "Should contain keys in files");
        assertTrue(test.get("mitko").getPasswords().containsKey(new WebsiteUserCombination("fb", "dimitar")),
                "Should contain record from file");
        assertTrue(test.get("pesho").getPasswords().containsKey(new WebsiteUserCombination("fb", "pesho")),
                "Should contain record from file");
        assertEquals("fbpassword",
                test.get("mitko").getPasswords().get(new WebsiteUserCombination("fb", "dimitar")),
                "Should return the correct password");
        assertEquals("fbppassword",
                test.get("pesho").getPasswords().get(new WebsiteUserCombination("fb", "pesho")),
                "Should return the correct password");
    }
}
