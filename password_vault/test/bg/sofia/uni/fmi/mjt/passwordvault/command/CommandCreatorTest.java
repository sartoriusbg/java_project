package bg.sofia.uni.fmi.mjt.passwordvault.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandCreatorTest {
    @Test
    void newCommandTest() {
        String[] args = {"mitko", "password", "password"};
        assertEquals(new Command("register",args),
                CommandCreator.newCommand("register mitko password password"),
                "Should return correct command from string");
    }

    @Test
    void newCommandWithQuotesTest() {
        String[] args = {"the funny website", "user"};
        assertEquals(new Command("generate-password", args),
                CommandCreator.newCommand("generate-password \"the funny website\" user"),
                "Should return correct command from string");
    }
}
