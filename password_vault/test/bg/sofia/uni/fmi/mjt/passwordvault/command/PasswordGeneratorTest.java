package bg.sofia.uni.fmi.mjt.passwordvault.command;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
public class PasswordGeneratorTest {
    private static final Character[] CHARACTERS = {'a' , 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n',
            'l', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'M', 'N', 'L', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '0', '!', '?', '[', ']', '~', '(', ')', ';', ':', '#', '$', '%', '^', '&',
            '*', '+', '='};
    @Test
    void generatePasswordTest() {
        String password = PasswordGenerator.generatePassword();
        Set<String> chars = new HashSet<>();
        chars.addAll(Arrays.stream(CHARACTERS).map(s -> s.toString()).toList());
        assertTrue(password.length() == 16);
        assertTrue(Arrays.stream(password.split("")).allMatch(c -> chars.contains(c)));
    }
}
