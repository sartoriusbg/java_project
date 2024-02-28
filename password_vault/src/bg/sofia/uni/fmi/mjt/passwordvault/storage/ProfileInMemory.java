package bg.sofia.uni.fmi.mjt.passwordvault.storage;

import java.util.HashMap;
import java.util.Map;

public class ProfileInMemory {
    private String password;
    private Map<WebsiteUserCombination, String> passwords;

    public ProfileInMemory(String username, String password) {
        this.password = password;
        this.passwords = new HashMap<>();
    }

    public ProfileInMemory(String username, String password, Map<WebsiteUserCombination, String> passwordsFromFile) {
        this.password = password;
        this.passwords = passwordsFromFile;
    }

    public void add(String website, String user, String password) {
        passwords.put(new WebsiteUserCombination(website, user), password);
    }

    public String getPassword() {
        return password;
    }

    public Map<WebsiteUserCombination, String> getPasswords() {
        return passwords;
    }

}
