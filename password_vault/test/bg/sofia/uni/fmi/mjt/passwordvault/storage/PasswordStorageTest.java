package bg.sofia.uni.fmi.mjt.passwordvault.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PasswordStorageTest {
    @InjectMocks
    private PasswordStorage test;

    @Mock
    private InMemoryPasswordStorage inMemoryMock;
    @Mock
    private FilePasswordStorage filesMock;

    @BeforeEach
    void setup() {
        inMemoryMock = mock(InMemoryPasswordStorage.class);
        filesMock = mock(FilePasswordStorage.class);
        test = new PasswordStorage(inMemoryMock, filesMock);
    }

    @Test
    void registerTest() {
        when(filesMock.register(anyString(),anyString(),anyString())).thenReturn(true);
        when(inMemoryMock.register(anyString(),anyString(),anyString())).thenReturn(true);
        assertTrue(test.register("profile", "password", "password"),
                "Should be true");
    }

    @Test
    void registerUnsuccessfulDueToFilesTest() {
        when(filesMock.register(anyString(),anyString(),anyString())).thenReturn(false);
        assertFalse(test.register("profile", "password", "password"),
                "Should be false");
    }


    @Test
    void registerUnsuccessfulDueToMemoryTest() {
        when(filesMock.register(anyString(),anyString(),anyString())).thenReturn(true);
        when(inMemoryMock.register(anyString(),anyString(),anyString())).thenReturn(false);
        assertFalse(test.register("profile", "password", "password"),
                "Should be false");
    }

    @Test
    void addPasswordTest() {
        when(filesMock.addPassword(anyString(),anyString(),anyString(),anyString())).thenReturn(true);
        when(inMemoryMock.addPassword(anyString(),anyString(),anyString(),anyString())).thenReturn(true);
        assertTrue(test.addPassword("profile", "website", "user", "password"),
                "Should be true");
    }

    @Test
    void addPasswordUnsuccessfulDueToFilesTest() {
        when(filesMock.addPassword(anyString(),anyString(),anyString(),anyString())).thenReturn(false);
        assertFalse(test.addPassword("profile", "website", "user", "password"),
                "Should be false");
    }


    @Test
    void addPasswordUnsuccessfulDueToMemoryTest() {
        when(filesMock.addPassword(anyString(),anyString(),anyString(),anyString())).thenReturn(true);
        when(inMemoryMock.addPassword(anyString(),anyString(),anyString(),anyString())).thenReturn(false);
        assertFalse(test.addPassword("profile", "website", "user", "password"),
                "Should be false");
    }

    @Test
    void removePasswordTest() {
        when(filesMock.removePassword(anyString(),anyString(),anyString())).thenReturn(true);
        when(inMemoryMock.removePassword(anyString(),anyString(),anyString())).thenReturn(true);
        assertTrue(test.removePassword("profile", "website", "user"),
                "Should be true");
    }

    @Test
    void removePasswordUnsuccessfulDueToFilesTest() {
        when(filesMock.removePassword(anyString(),anyString(),anyString())).thenReturn(false);
        assertFalse(test.removePassword("profile", "website", "user"),
                "Should be false");
    }


    @Test
    void removePasswordUnsuccessfulDueToMemoryTest() {
        when(filesMock.removePassword(anyString(),anyString(),anyString())).thenReturn(true);
        when(inMemoryMock.removePassword(anyString(),anyString(),anyString())).thenReturn(false);
        assertFalse(test.removePassword("profile", "password", "password"),
                "Should be false");
    }
    @Test
    void retrieveCredentialsTest() {
        when(inMemoryMock.retrieveCredentials(anyString(),anyString(),anyString())).thenReturn("result");
        assertEquals("result", test.retrieveCredentials("profile", "website", "user"),
                "Should return the right result");
    }
    @Test
    void userAlreadyExistsTest() {
        when(inMemoryMock.userAlreadyExists(anyString())).thenReturn(true);
        assertTrue(test.userAlreadyExists("profile"), "Should be true");
    }

    @Test
    void checkPasswordTest() {
        when(inMemoryMock.checkPassword(anyString(), anyString())).thenReturn(true);
        assertTrue(test.checkPassword("profile", "password"), "Should be true");
    }

    @Test
    void checkWebsiteUserCombinationTest() {
        when(inMemoryMock.checkWebsiteUserCombinationExists(anyString(), anyString(), anyString())).thenReturn(true);
        assertTrue(test.checkWebsiteUserCombinationExists("profile", "website", "user"),
                "Should be true");
    }
}
