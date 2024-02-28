package bg.sofia.uni.fmi.mjt.passwordvault.command;

import bg.sofia.uni.fmi.mjt.passwordvault.restapi.SecurePasswordApiClient;
import bg.sofia.uni.fmi.mjt.passwordvault.storage.PasswordStorage;
import bg.sofia.uni.fmi.mjt.passwordvault.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandExecutorTest {
    @InjectMocks
    private CommandExecutor test;

    @Mock
    private Storage storageMock;
    @Mock
    private SecurePasswordApiClient clientMock;

    @BeforeEach
    void setup() {
        clientMock = mock(SecurePasswordApiClient.class);
        storageMock = mock(PasswordStorage.class);
        test = new CommandExecutor(storageMock, clientMock);
    }

    @Test
    void executeUnknownCommandTest() throws URISyntaxException, NoSuchAlgorithmException {
        String[] args = {"jumboMumbo"};
        assertEquals("Unknown command", test.execute(new Command("mumboJumbo", args)),
                "Should return correct message for unknown command");
    }

    @Test
    void executeRegisterIllegalArgumentsTest() throws URISyntaxException, NoSuchAlgorithmException {
        String[] args = {"jumboMumbo"};
        assertEquals("Invalid count of arguments: \"register\" expects 3 arguments. Example: \"register <user> <password> <password-repeat>\"",
                test.execute(new Command("register", args)),
                "Should return correct message for wrong number of arguments");
    }

    @Test
    void executeRegisterInvalidNameTest() throws URISyntaxException, NoSuchAlgorithmException {
        String[] args = {"null", "password", "password", "null"};
        assertEquals("Cant have this name as profile name, try again",
                test.execute(new Command("register", args)),
                "Should return correct message for illegal name");
    }

    @Test
    void executeRegisterNotMatchingPasswords() throws URISyntaxException, NoSuchAlgorithmException {
        String[] args = {"profile", "password", "password1", "null"};
        assertEquals("The passwords don't match",
                test.execute(new Command("register", args)),
                "Should return correct message for not matching passwords");
    }

    @Test
    void executeRegisterUserAlreadyExists() throws URISyntaxException, NoSuchAlgorithmException {
        when(storageMock.userAlreadyExists(anyString())).thenReturn(true);
        String[] args = {"profile", "password", "password", "null"};
        assertEquals("Profile with this username is already in the system",
                test.execute(new Command("register", args)),
                "Should return correct message for not matching passwords");
    }
    @Test
    void executeRegisterUnsafePassword() throws URISyntaxException, NoSuchAlgorithmException {
        when(storageMock.userAlreadyExists(anyString())).thenReturn(false);
        when(clientMock.checkSecurePassword(anyString())).thenReturn(false);
        String[] args = {"profile", "password", "password", "null"};
        assertEquals("The given password is not secure, try again",
                test.execute(new Command("register", args)),
                "Should return correct message for not matching passwords");
    }
    @Test
    void executeRegisterUnsuccessful() throws URISyntaxException, NoSuchAlgorithmException {
        when(storageMock.userAlreadyExists(anyString())).thenReturn(false);
        when(clientMock.checkSecurePassword(anyString())).thenReturn(true);
        when(storageMock.register(anyString(),anyString(),anyString())).thenReturn(false);
        String[] args = {"profile", "password", "password", "null"};
        assertEquals("Registration unsuccessful",
                test.execute(new Command("register", args)),
                "Should return correct message for not matching passwords");
    }
     @Test
     void executeRegisterSuccessful() throws URISyntaxException, NoSuchAlgorithmException {
         when(storageMock.userAlreadyExists(anyString())).thenReturn(false);
         when(clientMock.checkSecurePassword(anyString())).thenReturn(true);
         when(storageMock.register(anyString(),anyString(),anyString())).thenReturn(true);
         String[] args = {"profile", "password", "password", "null"};
         assertEquals("Registration successful",
                 test.execute(new Command("register", args)),
                 "Should return correct message for not matching passwords");
     }

    @Test
    void executeLoginIllegalArgumentsTest() throws URISyntaxException, NoSuchAlgorithmException {
        String[] args = {"jumboMumbo"};
        assertEquals("Invalid count of arguments: \"login\" expects 2 arguments. Example: \"login <user> <password>\"",
                test.execute(new Command("login", args)),
                "Should return correct message for wrong number of arguments");
    }
    @Test
    void executeLoginUserDoesntExistTest() throws URISyntaxException, NoSuchAlgorithmException {
        when(storageMock.userAlreadyExists(anyString())).thenReturn(false);
        String[] args = {"profile", "password", "null"};
        assertEquals("Profile with this name doesn't exist",
                test.execute(new Command("login", args)),
                "Should return correct message for unexisting profile");
    }

    @Test
    void executeLoginWrongPassword() throws URISyntaxException, NoSuchAlgorithmException {
        when(storageMock.userAlreadyExists(anyString())).thenReturn(true);
        when(storageMock.checkPassword(anyString(), anyString())).thenReturn(false);
        String[] args = {"profile", "password", "null"};
        assertEquals("Wrong password",
                test.execute(new Command("login", args)),
                "Should return correct message for wrong password");
    }
    @Test
    void executeLoginSuccessful() throws URISyntaxException, NoSuchAlgorithmException {
        when(storageMock.userAlreadyExists(anyString())).thenReturn(true);
        when(storageMock.checkPassword(anyString(), anyString())).thenReturn(true);
        String[] args = {"profile", "password", "null"};
        assertEquals("Successful login",
                test.execute(new Command("login", args)),
                "Should return correct message for successful login");
    }

    @Test
    void executeLogoutIllegalArgumentsTest() throws URISyntaxException, NoSuchAlgorithmException {
        String[] args = {"jumboMumbo", "jumboMumbo"};
        assertEquals("Invalid count of arguments: \"logout\" expects 0 arguments. Example: \"logout\"",
                test.execute(new Command("logout", args)),
                "Should return correct message for wrong number of arguments");
    }

    @Test
    void executeLogoutSuccessfulTest() throws URISyntaxException, NoSuchAlgorithmException {
        String[] args = {"profile"};
        assertEquals("Successful logout",
                test.execute(new Command("logout", args)),
                "Should return correct message for successful logout");
    }

    @Test
    void executeRetrieveCredentialsIllegalArgumentsTest() throws URISyntaxException, NoSuchAlgorithmException {
        String[] args = {"jumboMumbo"};
        assertEquals("Invalid count of arguments: \"retrieve-credentials\" expects 2 arguments. Example: \"retrieve-credentials <website> <user>\"",
                test.execute(new Command("retrieve-credentials", args)),
                "Should return correct message for wrong number of arguments");
    }

    @Test
    void executeRetrieveCredentialsNotLoggedInTest() throws URISyntaxException, NoSuchAlgorithmException {
        String[] args = {"jumboMumbo" , "Mumbojumbo", "null"};
        assertEquals("You are not logged in. Please login first",
                test.execute(new Command("retrieve-credentials", args)),
                "Should return correct message for not logged in user");
    }

    @Test
    void executeRetrieveCredentialsNoSuchRecordTest() throws URISyntaxException, NoSuchAlgorithmException {
        when(storageMock.checkWebsiteUserCombinationExists(anyString(),anyString(),anyString())).thenReturn(false);
        String[] args = {"jumboMumbo" , "Mumbojumbo", "profile"};
        assertEquals("No such record",
                test.execute(new Command("retrieve-credentials", args)),
                "Should return correct message for no such record");
    }
    @Test
    void executeRetrieveCredentialsProblemWithEncryptionTest() throws URISyntaxException, NoSuchAlgorithmException {
        when(storageMock.checkWebsiteUserCombinationExists(anyString(),anyString(),anyString())).thenReturn(true);
        when(storageMock.retrieveCredentials(anyString(),anyString(),anyString())).thenReturn("password");
        String[] args = {"jumboMumbo" , "Mumbojumbo", "profile"};
        assertEquals("Problem with encryption/decryption",
                test.execute(new Command("retrieve-credentials", args)),
                "Should return correct message for encryption problem");
        Path.of("profileerrors.txt").toFile().delete();
        Path.of("ServerLogs.txt").toFile().delete();
    }
    @Test
    void executeRetrieveCredentialsTest() throws URISyntaxException, NoSuchAlgorithmException {
        when(storageMock.checkWebsiteUserCombinationExists(anyString(),anyString(),anyString())).thenReturn(true);
        when(storageMock.retrieveCredentials(anyString(),anyString(),anyString())).thenReturn("5eTqqF8Sp3WGqiq+oMqWpQ==");
        String[] args = {"jumboMumbo" , "Mumbojumbo", "profile"};
        assertEquals("password",
                test.execute(new Command("retrieve-credentials", args)),
                "Should return correct password");
    }


    @Test
    void executeGeneratePasswordIllegalArgumentsTest() throws URISyntaxException, NoSuchAlgorithmException {
        String[] args = {"jumboMumbo"};
        assertEquals("Invalid count of arguments: \"generate-password\" expects 2 arguments. Example: \"generate-password <website> <user>\"",
                test.execute(new Command("generate-password", args)),
                "Should return correct message for wrong number of arguments");
    }

    @Test
    void executeGeneratePasswordNotLoggedInTest() throws URISyntaxException, NoSuchAlgorithmException {
        String[] args = {"jumboMumbo" , "Mumbojumbo", "null"};
        assertEquals("You are not logged in. Please login first",
                test.execute(new Command("generate-password", args)),
                "Should return correct message for user not logged in");
    }

    @Test
    void executeGeneratePasswordTest() throws URISyntaxException, NoSuchAlgorithmException {
        when(storageMock.addPassword(anyString(),anyString(),anyString(), anyString())).thenReturn(true);
        when(clientMock.checkSecurePassword(anyString())).thenReturn(true);
        String[] args = {"jumboMumbo" , "Mumbojumbo", "profile"};
        assertEquals(16,
                test.execute(new Command("generate-password", args)).length(),
                "Should return correct length string");
    }

    @Test
    void executeAddPasswordIllegalArgumentsTest() throws URISyntaxException, NoSuchAlgorithmException {
        String[] args = {"jumboMumbo"};
        assertEquals("Invalid count of arguments: \"add-password\" expects 3 arguments. Example: \"add-password <website> <user> <password>\"",
                test.execute(new Command("add-password", args)),
                "Should return correct message for wrong number of arguments");
    }

    @Test
    void executeAddPasswordNotLoggedInTest() throws URISyntaxException, NoSuchAlgorithmException {
        String[] args = {"jumboMumbo" , "Mumbojumbo", "password", "null"};
        assertEquals("You are not logged in. Please login first",
                test.execute(new Command("add-password", args)),
                "Should return correct message for user not logged in");
    }

    @Test
    void executeAddPasswordNoSuchRecordTest() throws URISyntaxException, NoSuchAlgorithmException {
        when(storageMock.addPassword(anyString(),anyString(),anyString(), anyString())).thenReturn(true);
        when(clientMock.checkSecurePassword(anyString())).thenReturn(true);
        String[] args = {"jumboMumbo" , "Mumbojumbo", "password", "profile"};
        assertEquals("Successfully added password",
                test.execute(new Command("add-password", args)),
                "Should return message for successful operation");
    }
    /*private String removePassword(String[] arguments) {
        if(arguments.length != NEEDED_ARGUMENTS_2 + PROFILE_OFFSET) {
            return String.format(INVALID_ARGS_COUNT_MESSAGE_FORMAT, REMOVE_PASSWORD, NEEDED_ARGUMENTS_2,
                    REMOVE_PASSWORD_EXAMPLE);
        }
        if(arguments[2].equals(NOT_LOGGED_PROFILE)) {
            return NOT_LOGGED_IN;
        }
        if(! storage.checkWebsiteUserCombinationExists(arguments[2], arguments[0], arguments[1])) {
            return NO_SUCH_RECORD_MESSAGE;
        }
        if(storage.removePassword(arguments[2], arguments[0], arguments[1])) {
            return REMOVE_PASSWORD_SUCCESSFUL;
        }
        else {
            return REMOVE_PASSWORD_UNSUCCESSFUL;
        }
    }*/
    @Test
    void executeRemovePasswordIllegalArgumentsTest() throws URISyntaxException, NoSuchAlgorithmException {
        String[] args = {"jumboMumbo"};
        assertEquals("Invalid count of arguments: \"remove-password\" expects 2 arguments. Example: \"remove-password <website> <user>\"",
                test.execute(new Command("remove-password", args)),
                "Should return correct message for wrong number of arguments");
    }

    @Test
    void executeRemovePasswordNotLoggedInTest() throws URISyntaxException, NoSuchAlgorithmException {
        String[] args = {"jumboMumbo" , "Mumbojumbo", "null"};
        assertEquals("You are not logged in. Please login first",
                test.execute(new Command("remove-password", args)),
                "Should return correct message for user not logged in");
    }
    @Test
    void executeRemovePasswordNoSuchRecordTest() throws URISyntaxException, NoSuchAlgorithmException {
        when(storageMock.checkWebsiteUserCombinationExists(anyString(),anyString(),anyString())).thenReturn(false);
        String[] args = {"jumboMumbo" , "Mumbojumbo", "profile"};
        assertEquals("No such record",
                test.execute(new Command("remove-password", args)),
                "Should return correct message for no such record");
    }
    @Test
    void executeRemovePasswordUnsuccessful() throws URISyntaxException, NoSuchAlgorithmException {
        when(storageMock.checkWebsiteUserCombinationExists(anyString(),anyString(),anyString())).thenReturn(true);
        when(storageMock.removePassword(anyString(),anyString(),anyString())).thenReturn(false);
        String[] args = {"jumboMumbo" , "Mumbojumbo", "profile"};
        assertEquals("Password unsuccessfully removed",
                test.execute(new Command("remove-password", args)),
                "Should return correct message for no such record");
    }
    @Test
    void executeRemovePasswordSuccessful() throws URISyntaxException, NoSuchAlgorithmException {
        when(storageMock.checkWebsiteUserCombinationExists(anyString(),anyString(),anyString())).thenReturn(true);
        when(storageMock.removePassword(anyString(),anyString(),anyString())).thenReturn(true);
        String[] args = {"jumboMumbo" , "Mumbojumbo", "profile"};
        assertEquals("Password successfully removed",
                test.execute(new Command("remove-password", args)),
                "Should return correct message for no such record");
    }
}
