package bg.sofia.uni.fmi.mjt.passwordvault.converters;

import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringEncryptorTest {
    @Test
    void encryptTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        assertEquals("5eTqqF8Sp3WGqiq+oMqWpQ==", StringEncryptor.encrypt("password"),
                "Should encrypt with the correct result");
    }

    @Test
    void decryptTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        assertEquals("password", StringEncryptor.decrypt("5eTqqF8Sp3WGqiq+oMqWpQ=="),
                "Should decrypt with the correct result");
    }
}
