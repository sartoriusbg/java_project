package bg.sofia.uni.fmi.mjt.passwordvault.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfileInMemoryTest {
    private ProfileInMemory test;

    @BeforeEach
    void setup() {
        test = new ProfileInMemory("username", "password");
    }

    @Test
    void addTest() {
        test.add("moodle", "dh", "passwd");
        assertEquals("passwd", test.getPasswords().get(new WebsiteUserCombination("moodle", "dh")),
                "Should have the record in the memory");
    }
}
