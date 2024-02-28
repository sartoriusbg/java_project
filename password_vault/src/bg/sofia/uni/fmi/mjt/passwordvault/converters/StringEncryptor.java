package bg.sofia.uni.fmi.mjt.passwordvault.converters;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class StringEncryptor {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String AES = "AES";
    private static final byte[] IV_GENERATOR = {24, -83, 74, -16, -123, -48, -34, 108, -122, 100, -3, -93, -2, 31
            , -57, 59};
    private static final byte[] KEY_GENERATOR = {-110, 81, -77, -106, -45, 71, 41, 125, -117, -108, -58, 71, 26, 107
            , 63, 82};
    private static final IvParameterSpec IV = new IvParameterSpec(IV_GENERATOR);
    private static final SecretKey KEY = new SecretKeySpec(KEY_GENERATOR, AES);

    public static String encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, KEY, IV);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public static String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, KEY, IV);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }
}
