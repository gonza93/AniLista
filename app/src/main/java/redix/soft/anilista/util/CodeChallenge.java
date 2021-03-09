package redix.soft.anilista.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import android.util.Base64;

public class CodeChallenge {

    public static String generateCodeVerifier() throws NoSuchAlgorithmException {
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[32];
        sr.nextBytes(code);
        String verifier = Base64.encodeToString(code, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
        return verifier
                .replace("=", "")
                .replace("+", "-")
                .replace("/", "_");
    }

    public static String generateCodeChallange(String verifier) throws NoSuchAlgorithmException {
        byte[] bytes = verifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        return Base64.encodeToString(digest, Base64.DEFAULT)
                .replace("=", "")
                .replace("+", "-")
                .replace("/", "_");
    }

}
