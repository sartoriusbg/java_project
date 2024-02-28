package bg.sofia.uni.fmi.mjt.passwordvault.converters;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class StringHasherTest {

    @Test
    void toHashTest() throws NoSuchAlgorithmException {
        assertEquals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8", StringHasher.toHash("password", "SHA-256"));
        assertEquals("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8", StringHasher.toHash("password", "SHA-1"));
        assertEquals("5f4dcc3b5aa765d61d8327deb882cf99", StringHasher.toHash("password", "MD5"));
    }

    @Test
    void toHashWrongAlgorithmTest() {
        assertThrows(NoSuchAlgorithmException.class, () -> StringHasher.toHash("password", "funnyAlgorithm"));
    }
}
