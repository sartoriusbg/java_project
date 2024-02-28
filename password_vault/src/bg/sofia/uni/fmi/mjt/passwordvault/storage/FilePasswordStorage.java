package bg.sofia.uni.fmi.mjt.passwordvault.storage;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FilePasswordStorage implements FileStorage {
    private static final Path PROFILES_FILE = Path.of("profiles.txt");
    private static final String PASSWORDS_FILE_NAMES = "passwords.txt";
    private static final String ERRORS_FILE_NAMES = "errors.txt";
    private static final Path TEMP_FILE = Path.of("temp.txt");
    private Map<String, ProfileFile> profileFileMap;

    public FilePasswordStorage() {
        profileFileMap = new HashMap<>();
    }

    public FilePasswordStorage(Map<String, ProfileFile> fromFile) {
        this.profileFileMap = fromFile;
    }

    @Override
    public boolean register(String profile, String password, String passwordRepeat) {
        try (Writer writer = new BufferedWriter(new FileWriter(PROFILES_FILE.toString(), true));
             Writer writerPasswords = new BufferedWriter(new FileWriter(profile + PASSWORDS_FILE_NAMES))) {
            writer.write(profile + " " + password + System.lineSeparator());
            writer.flush();
            writerPasswords.write("");
            writerPasswords.flush();
        } catch (IOException e) {
            return false;
        }
        profileFileMap.put(profile, new ProfileFile(password, Path.of(profile + PASSWORDS_FILE_NAMES),
                Path.of(profile + ERRORS_FILE_NAMES)));
        return true;
    }

    @Override
    public boolean addPassword(String profile, String website, String user, String password) {
        try (Writer writer = new BufferedWriter(new FileWriter(profileFileMap.get(profile).passwords().toString(),
                true))) {
            writer.write(website + " " + user + " " + password + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean removePassword(String profile, String website, String user) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(profileFileMap.get(profile).passwords().toString()));
             Writer writer = new BufferedWriter(new FileWriter(TEMP_FILE.toString(), true))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(website + " " + user)) {
                    continue;
                }
                writer.write(line + System.lineSeparator());
                writer.flush();

            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        profileFileMap.get(profile).passwords().toFile().delete();
        return TEMP_FILE.toFile().renameTo(profileFileMap.get(profile).passwords().toFile());
    }

}
