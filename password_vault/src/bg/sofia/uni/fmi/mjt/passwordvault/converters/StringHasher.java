package bg.sofia.uni.fmi.mjt.passwordvault.converters;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringHasher {
    public static String toHash(String word, String hash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(hash);
        md.update(word.getBytes(StandardCharsets.UTF_8));
        String result = bytesToHex(md.digest());
        return result;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
