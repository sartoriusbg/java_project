package bg.sofia.uni.fmi.mjt.passwordvault.command;

import bg.sofia.uni.fmi.mjt.passwordvault.converters.StringEncryptor;
import bg.sofia.uni.fmi.mjt.passwordvault.converters.StringHasher;
import bg.sofia.uni.fmi.mjt.passwordvault.errorhandler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.passwordvault.exceptions.EncryptorException;
import bg.sofia.uni.fmi.mjt.passwordvault.exceptions.HashingAlgorithException;
import bg.sofia.uni.fmi.mjt.passwordvault.restapi.SecurePasswordApiClient;
import bg.sofia.uni.fmi.mjt.passwordvault.storage.Storage;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public class CommandExecutor {
    private static final String INVALID_ARGS_COUNT_MESSAGE_FORMAT =
            "Invalid count of arguments: \"%s\" expects %d arguments. Example: \"%s\"";

    private static final String REGISTER = "register";
    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String RETRIEVE_CREDENTIALS = "retrieve-credentials";
    private static final String GENERATE_PASSWORD = "generate-password";
    private static final String ADD_PASSWORD = "add-password";
    private static final String REMOVE_PASSWORD = "remove-password";
    private static final String UNKNOWN_MESSAGE = "Unknown command";
    private static final int NEEDED_ARGUMENTS_3 = 3;
    private static final int NEEDED_ARGUMENTS_2 = 2;
    private static final int NEEDED_ARGUMENTS_0 = 0;
    private static final String REGISTER_EXAMPLE = "register <user> <password> <password-repeat>";
    private static final String REGISTER_PASSWORD_NOT_MATCHING_MESSAGE = "The passwords don't match";
    private static final String REGISTRATION_SUCCESSFUL_MESSAGE = "Registration successful";
    private static final String REGISTRATION_UNSUCCESSFUL_MESSAGE = "Registration unsuccessful";
    private static final String REGISTRATION_USER_ALREADY_EXISTS =
            "Profile with this username is already in the system";
    private static final String LOGIN_EXAMPLE = "login <user> <password>";
    private static final String LOGIN_WRONG_PASSWORD = "Wrong password";
    private static final String LOGIN_NO_SUCH_PROFILE = "Profile with this name doesn't exist";
    private static final String LOGIN_SUCCESSFUL = "Successful login";
    private static final String LOGIN_HASHING_PROBLEM = "Login unsuccessful due to hashing problem";
    private static final String LOGOUT_SUCCESSFUL = "Successful logout";
    private static final String NOT_LOGGED_IN = "You are not logged in. Please login first";
    private static final String RETRIEVE_CREDENTIALS_EXAMPLE = "retrieve-credentials <website> <user>";
    private static final String GENERATE_PASSWORD_EXAMPLE = "generate-password <website> <user>";
    private static final String ADD_PASSWORD_EXAMPLE = "add-password <website> <user> <password>";
    private static final String ADD_PASSWORD_SUCCESSFUL = "Successfully added password";
    private static final String ADD_PASSWORD_UNSUCCESSFUL = "Unsuccessfully added password";
    private static final String REMOVE_PASSWORD_EXAMPLE = "remove-password <website> <user>";
    private static final String REMOVE_PASSWORD_SUCCESSFUL = "Password successfully removed";
    private static final String REMOVE_PASSWORD_UNSUCCESSFUL = "Password unsuccessfully removed";
    private static final String PASSWORD_NOT_SECURE = "The given password is not secure, try again";
    private static final String HASHING_ALGORITHM = "SHA-256";
    private static final String NO_SUCH_RECORD_MESSAGE = "No such record";
    private static final String NOT_LOGGED_PROFILE = "null";
    private static final String INVALID_NAME_MESSAGE = "Cant have this name as profile name, try again";
    private static final int PROFILE_OFFSET = 1;
    private static final String ENCRYPTOR_PROBLEM_MESSAGE = "Problem with encryption/decryption";
    private static final String ERRORS_FILE_NAMES = "errors.txt";
    private static final int ARGUMENTS_ZERO = 0;
    private static final int ARGUMENTS_ONE = 1;
    private static final int ARGUMENTS_TWO = 2;
    private static final int ARGUMENTS_THREE = 3;

    private Storage storage;
    private SecurePasswordApiClient client;

    public CommandExecutor(Storage storage, SecurePasswordApiClient client) {
        this.storage = storage;
        this.client = client;
    }

    public String execute(Command cmd) throws URISyntaxException, NoSuchAlgorithmException {
        return switch (cmd.command()) {
            case REGISTER -> register(cmd.arguments());
            case LOGIN -> login(cmd.arguments());
            case LOGOUT -> logout(cmd.arguments());
            case RETRIEVE_CREDENTIALS -> retrieveCredentials(cmd.arguments());
            case GENERATE_PASSWORD -> generatePassword(cmd.arguments());
            case ADD_PASSWORD -> addPassword(cmd.arguments());
            case REMOVE_PASSWORD -> removePassword(cmd.arguments());
            default -> UNKNOWN_MESSAGE;
        };
    }

    private String register(String[] arguments) throws URISyntaxException, NoSuchAlgorithmException {
        if (arguments.length != NEEDED_ARGUMENTS_3 + PROFILE_OFFSET) {
            return String.format(INVALID_ARGS_COUNT_MESSAGE_FORMAT, REGISTER, NEEDED_ARGUMENTS_3,
                    REGISTER_EXAMPLE);
        }
        if (arguments[ARGUMENTS_ZERO].equals(NOT_LOGGED_PROFILE)) {
            return INVALID_NAME_MESSAGE;
        }
        if (!arguments[ARGUMENTS_ONE].equals(arguments[ARGUMENTS_TWO])) {
            return REGISTER_PASSWORD_NOT_MATCHING_MESSAGE;
        }
        if (storage.userAlreadyExists(arguments[ARGUMENTS_ZERO])) {
            return REGISTRATION_USER_ALREADY_EXISTS;
        }
        if (!client.checkSecurePassword(arguments[ARGUMENTS_ONE])) {
            return PASSWORD_NOT_SECURE;
        }
        try {
            if (storage.register(arguments[ARGUMENTS_ZERO],
                    StringHasher.toHash(arguments[ARGUMENTS_ONE], HASHING_ALGORITHM),
                    StringHasher.toHash(arguments[ARGUMENTS_TWO], HASHING_ALGORITHM))) {
                return REGISTRATION_SUCCESSFUL_MESSAGE;
            } else {
                return REGISTRATION_UNSUCCESSFUL_MESSAGE;
            }
        } catch (NoSuchAlgorithmException e) {
            HashingAlgorithException ex = new HashingAlgorithException();
            ErrorHandler.writeLogs(ex);
            throw ex;
        }
    }

    private String login(String[] arguments) {
        if (arguments.length != NEEDED_ARGUMENTS_2 + PROFILE_OFFSET) {
            return String.format(INVALID_ARGS_COUNT_MESSAGE_FORMAT, LOGIN, NEEDED_ARGUMENTS_2,
                    LOGIN_EXAMPLE);
        }
        if (!storage.userAlreadyExists(arguments[ARGUMENTS_ZERO])) {
            return LOGIN_NO_SUCH_PROFILE;
        }
        try {
            if (!storage.checkPassword(arguments[ARGUMENTS_ZERO],
                    StringHasher.toHash(arguments[ARGUMENTS_ONE], HASHING_ALGORITHM))) {
                return LOGIN_WRONG_PASSWORD;
            }
        } catch (NoSuchAlgorithmException e) {
            HashingAlgorithException ex = new HashingAlgorithException();
            ErrorHandler.writeLogs(ex);
            return LOGIN_HASHING_PROBLEM;
        }
        return LOGIN_SUCCESSFUL;
    }

    private String logout(String[] arguments) {
        if (arguments.length != NEEDED_ARGUMENTS_0 + PROFILE_OFFSET) {
            return String.format(INVALID_ARGS_COUNT_MESSAGE_FORMAT, LOGOUT, NEEDED_ARGUMENTS_0,
                    LOGOUT);
        }
        return LOGOUT_SUCCESSFUL;
    }

    private String retrieveCredentials(String[] arguments) {
        if (arguments.length != NEEDED_ARGUMENTS_2 + PROFILE_OFFSET) {
            return String.format(INVALID_ARGS_COUNT_MESSAGE_FORMAT, RETRIEVE_CREDENTIALS, NEEDED_ARGUMENTS_2,
                    RETRIEVE_CREDENTIALS_EXAMPLE);
        }
        if (arguments[2].equals(NOT_LOGGED_PROFILE)) {
            return NOT_LOGGED_IN;
        }
        if (!storage.checkWebsiteUserCombinationExists(arguments[2], arguments[0], arguments[1])) {
            return NO_SUCH_RECORD_MESSAGE;
        }
        try {
            return StringEncryptor.decrypt(storage.retrieveCredentials(arguments[2], arguments[0], arguments[1]));
        } catch (Exception e) {
            EncryptorException ex = new EncryptorException();
            ErrorHandler.writeLogs(ex);
            ErrorHandler.writeLogs(ex, Path.of(arguments[2] + ERRORS_FILE_NAMES));
            return ENCRYPTOR_PROBLEM_MESSAGE;
        }
    }

    private String generatePassword(String[] arguments) throws URISyntaxException, NoSuchAlgorithmException {

        if (arguments.length != NEEDED_ARGUMENTS_2 + PROFILE_OFFSET) {
            return String.format(INVALID_ARGS_COUNT_MESSAGE_FORMAT, GENERATE_PASSWORD, NEEDED_ARGUMENTS_2,
                    GENERATE_PASSWORD_EXAMPLE);
        }
        if (arguments[2].equals(NOT_LOGGED_PROFILE)) {
            return NOT_LOGGED_IN;
        }
        String password = PasswordGenerator.generatePassword();
        while (!client.checkSecurePassword(password)) {
            password = PasswordGenerator.generatePassword();
        }
        try {
            storage.addPassword(arguments[2], arguments[0], arguments[1], StringEncryptor.encrypt(password));
        } catch (Exception e) {
            EncryptorException ex = new EncryptorException();
            ErrorHandler.writeLogs(ex);
            ErrorHandler.writeLogs(ex, Path.of(arguments[2] + ERRORS_FILE_NAMES));
            return ENCRYPTOR_PROBLEM_MESSAGE;
        }
        return password;
    }

    private String addPassword(String[] arguments) throws URISyntaxException, NoSuchAlgorithmException {
        if (arguments.length != NEEDED_ARGUMENTS_3 + PROFILE_OFFSET) {
            return String.format(INVALID_ARGS_COUNT_MESSAGE_FORMAT, ADD_PASSWORD, NEEDED_ARGUMENTS_3,
                    ADD_PASSWORD_EXAMPLE);
        }
        if (arguments[ARGUMENTS_THREE].equals(NOT_LOGGED_PROFILE)) {
            return NOT_LOGGED_IN;
        }
        if (!client.checkSecurePassword(arguments[ARGUMENTS_TWO])) {
            return PASSWORD_NOT_SECURE;
        }
        try {
            if (storage.addPassword(arguments[ARGUMENTS_THREE], arguments[ARGUMENTS_ZERO], arguments[ARGUMENTS_ONE],
                    StringEncryptor.encrypt(arguments[ARGUMENTS_TWO]))) {
                return ADD_PASSWORD_SUCCESSFUL;
            } else {
                return ADD_PASSWORD_UNSUCCESSFUL;
            }
        } catch (Exception e) {
            EncryptorException ex = new EncryptorException();
            ErrorHandler.writeLogs(ex);
            ErrorHandler.writeLogs(ex, Path.of(arguments[2] + ERRORS_FILE_NAMES));
            return ENCRYPTOR_PROBLEM_MESSAGE;
        }
    }

    private String removePassword(String[] arguments) {
        if (arguments.length != NEEDED_ARGUMENTS_2 + PROFILE_OFFSET) {
            return String.format(INVALID_ARGS_COUNT_MESSAGE_FORMAT, REMOVE_PASSWORD, NEEDED_ARGUMENTS_2,
                    REMOVE_PASSWORD_EXAMPLE);
        }
        if (arguments[ARGUMENTS_TWO].equals(NOT_LOGGED_PROFILE)) {
            return NOT_LOGGED_IN;
        }
        if (!storage.checkWebsiteUserCombinationExists(arguments[ARGUMENTS_TWO], arguments[ARGUMENTS_ZERO],
                arguments[ARGUMENTS_ONE])) {
            return NO_SUCH_RECORD_MESSAGE;
        }
        if (storage.removePassword(arguments[ARGUMENTS_TWO], arguments[ARGUMENTS_ZERO], arguments[ARGUMENTS_ONE])) {
            return REMOVE_PASSWORD_SUCCESSFUL;
        } else {
            return REMOVE_PASSWORD_UNSUCCESSFUL;
        }
    }
}
