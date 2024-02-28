package bg.sofia.uni.fmi.mjt.passwordvault.storage;

public interface FileStorage {
    boolean register(String profile, String password, String passwordRepeat);
    boolean addPassword(String profile, String website, String user, String password);
    boolean removePassword(String profile, String website, String user);
}
