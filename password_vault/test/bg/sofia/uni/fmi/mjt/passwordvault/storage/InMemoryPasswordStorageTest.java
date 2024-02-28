package bg.sofia.uni.fmi.mjt.passwordvault.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryPasswordStorageTest {
    private InMemoryPasswordStorage test;

    @BeforeEach
    void setup() {
        test = new InMemoryPasswordStorage();
    }

    @Test
    void registerTest() {
        assertTrue(test.register("profile", "password", "password"),
                "Should be true for successful registration");
    }

    @Test
    void addPasswordTest() {
        test.register("profile", "password", "password");
        assertTrue(test.addPassword("profile", "website", "user", "password"),
                "Should be true for successful adding of password");
    }

    @Test
    void retrieveCredentialsTest() {
        test.register("profile", "password", "password");
        test.addPassword("profile", "website", "user", "password");
        assertEquals("password", test.retrieveCredentials("profile", "website", "user"),
                "Should get the correct password");
    }
    @Test
    void retrieveCredentialsNoResultTest() {
        test.register("profile", "password", "password");
        assertEquals("No such result", test.retrieveCredentials("profile", "website", "user"),
                "Should get the the message for not existing record");
    }

    @Test
    void checkUserAlreadyExistsTest() {
        test.register("profile", "password", "password");
        assertTrue(test.userAlreadyExists("profile"), "Should return true for existing user");
        assertFalse(test.userAlreadyExists("profile2"), "Should return false for non existing profile");
    }

    @Test
    void checkWebsiteUserCombinationExistsTest() {
        test.register("profile", "password", "password");
        test.addPassword("profile", "website", "user", "password");
        assertTrue(test.checkWebsiteUserCombinationExists("profile", "website", "user"),
                "Should return true for existing user");
        assertFalse(test.checkWebsiteUserCombinationExists("profile", "website2", "user"),
                "Should return false for non existing profile");
        assertFalse(test.checkWebsiteUserCombinationExists("profile", "website", "user2"),
                "Should return false for non existing profile");

    }
    @Test
    void removePasswordTest() {
        test.register("profile", "password", "password");
        test.addPassword("profile", "website", "user", "password");
        assertTrue(test.removePassword("profile", "website", "user"),
                "Should return true");
    }

    @Test
    void checkPasswordTest() {
        test.register("profile", "password", "password");
        assertTrue(test.checkPassword("profile", "password"),
                "Should return true for correct password");
        assertFalse(test.checkPassword("profile", "password2"));
    }
}
