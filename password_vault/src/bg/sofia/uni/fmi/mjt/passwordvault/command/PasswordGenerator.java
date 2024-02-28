package bg.sofia.uni.fmi.mjt.passwordvault.command;

import java.util.Random;

public class PasswordGenerator {
    private static final int PASSWORD_LENGTH = 16;
    private static final Character[] CHARACTERS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n'
            , 'l', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'
            , 'I', 'J', 'K', 'M', 'N', 'L', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3'
            , '4', '5', '6', '7', '8', '9', '0', '!', '?', '[', ']', '~', '(', ')', ';', ':', '#', '$', '%', '^', '&'
            , '*', '+', '='};

    public static String generatePassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS[Math.abs(random.nextInt()) % CHARACTERS.length]);
        }
        return password.toString();
    }
}
