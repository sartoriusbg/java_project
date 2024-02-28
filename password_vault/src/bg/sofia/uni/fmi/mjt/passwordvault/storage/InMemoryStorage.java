package bg.sofia.uni.fmi.mjt.passwordvault.storage;


public interface InMemoryStorage {
    boolean register(String profile, String password, String passwordRepeat);
    boolean addPassword(String profile, String website, String user, String password) ;
    boolean removePassword(String profile, String website, String user);
    String retrieveCredentials(String profile, String website, String user);
    boolean userAlreadyExists(String profile);
    boolean checkPassword(String profile, String password);

    boolean checkWebsiteUserCombinationExists(String profile, String website, String user);
}
