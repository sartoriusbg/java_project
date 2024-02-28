package bg.sofia.uni.fmi.mjt.passwordvault.storage;

import java.nio.file.Path;

public record ProfileFile(String password, Path passwords, Path errors) {
}
