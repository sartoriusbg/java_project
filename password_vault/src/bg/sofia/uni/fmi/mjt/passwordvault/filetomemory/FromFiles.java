package bg.sofia.uni.fmi.mjt.passwordvault.filetomemory;

import bg.sofia.uni.fmi.mjt.passwordvault.storage.ProfileFile;
import bg.sofia.uni.fmi.mjt.passwordvault.storage.ProfileInMemory;
import bg.sofia.uni.fmi.mjt.passwordvault.storage.WebsiteUserCombination;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FromFiles {
    private static final Path PROFILES_FILE = Path.of("profiles.txt");
    private static final String PASSWORDS_FILE_NAMES = "passwords.txt";
    private static final String ERRORS_FILE_NAMES = "errors.txt";

    public static boolean profilesExists() {
        return PROFILES_FILE.toFile().exists();
    }

    public static Map<String, ProfileFile> getProfilesFileMap() {
        Map<String, ProfileFile> result = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PROFILES_FILE.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split(" ");
                result.put(args[0], new ProfileFile(args[1], Path.of(args[0] + PASSWORDS_FILE_NAMES),
                        Path.of(args[0] + ERRORS_FILE_NAMES)));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static Map<String, ProfileInMemory> getProfileMemoryMap() {
        Map<String, ProfileInMemory> result = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PROFILES_FILE.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split(" ");
                Map<WebsiteUserCombination, String> toAdd = new HashMap<>();
                try (BufferedReader passwordsReader = new BufferedReader(new FileReader(args[0]
                        + PASSWORDS_FILE_NAMES))) {
                    String passwordRecord;
                    while ((passwordRecord = passwordsReader.readLine()) != null) {
                        String[] record = passwordRecord.split(" ");
                        toAdd.put(new WebsiteUserCombination(record[0], record[1]), record[2]);
                    }
                }
                result.put(args[0], new ProfileInMemory(args[0], args[1], toAdd));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
