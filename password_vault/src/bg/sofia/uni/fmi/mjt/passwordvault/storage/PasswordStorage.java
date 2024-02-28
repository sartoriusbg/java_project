package bg.sofia.uni.fmi.mjt.passwordvault.storage;

import bg.sofia.uni.fmi.mjt.passwordvault.filetomemory.FromFiles;

import java.nio.file.Path;

public class PasswordStorage implements Storage {

    private InMemoryStorage inMemory;
    private FileStorage files;

    public PasswordStorage() {
        inMemory = new InMemoryPasswordStorage();
        files = new FilePasswordStorage();
    }

    public PasswordStorage(Path profileFilesFile) {
        inMemory = new InMemoryPasswordStorage(FromFiles.getProfileMemoryMap());
        files = new FilePasswordStorage(FromFiles.getProfilesFileMap());
    }

    PasswordStorage(InMemoryPasswordStorage inMemory, FilePasswordStorage files) {
        this.inMemory = inMemory;
        this.files = files;
    }
    @Override
    public boolean register(String profile, String password, String passwordRepeat) {
        if (files.register(profile, password, passwordRepeat)) {
            if (inMemory.register(profile, password, passwordRepeat)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addPassword(String profile, String website, String user, String password) {
        if (files.addPassword(profile, website, user, password)) {
            if (inMemory.addPassword(profile, website, user, password)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean removePassword(String profile, String website, String user) {
        if (files.removePassword(profile, website, user)) {
            if (inMemory.removePassword(profile, website, user)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String retrieveCredentials(String profile, String website, String user) {
        return inMemory.retrieveCredentials(profile, website, user);
    }

    @Override
    public boolean userAlreadyExists(String profile) {
        return inMemory.userAlreadyExists(profile);
    }

    @Override
    public boolean checkPassword(String profile, String password) {
        return inMemory.checkPassword(profile, password);
    }

    @Override
    public boolean checkWebsiteUserCombinationExists(String profile, String website, String user) {
        return inMemory.checkWebsiteUserCombinationExists(profile, website, user);
    }


}
