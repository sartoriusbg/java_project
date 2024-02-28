package bg.sofia.uni.fmi.mjt.passwordvault.storage;

import java.util.HashMap;
import java.util.Map;

public class InMemoryPasswordStorage implements InMemoryStorage {
    private static final String NO_SUCH_RESULT = "No such result";
    private Map<String, ProfileInMemory> profiles;

    public InMemoryPasswordStorage() {
        this.profiles = new HashMap<>();
    }

    public InMemoryPasswordStorage(Map<String, ProfileInMemory> fromFIle) {
        this.profiles = fromFIle;
    }

    @Override
    public boolean register(String profile, String password, String passwordRepeat) {
        profiles.put(profile, new ProfileInMemory(profile, password));
        return true;
    }

    @Override
    public boolean addPassword(String profile, String website, String user, String password) {
        profiles.get(profile).add(website, user, password);
        return true;
    }


    @Override
    public boolean removePassword(String profile, String website, String user) {
        profiles.get(profile).getPasswords().remove(new WebsiteUserCombination(website, user));
        return true;
    }

    @Override
    public String retrieveCredentials(String profile, String website, String user) {
        return profiles.get(profile).getPasswords().getOrDefault(new WebsiteUserCombination(website, user),
                NO_SUCH_RESULT);
    }

    @Override
    public boolean userAlreadyExists(String profile) {
        return profiles.containsKey(profile);
    }

    @Override
    public boolean checkPassword(String profile, String password) {
        return profiles.get(profile).getPassword().equals(password);
    }

    @Override
    public boolean checkWebsiteUserCombinationExists(String profile, String website, String user) {
        return profiles.get(profile).getPasswords().containsKey(new WebsiteUserCombination(website, user));
    }

}
