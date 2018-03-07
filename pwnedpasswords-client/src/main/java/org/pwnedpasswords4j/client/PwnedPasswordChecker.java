package org.pwnedpasswords4j.client;

import okhttp3.OkHttpClient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class PwnedPasswordChecker {

    private static OkHttpClient httpClient = new OkHttpClient();
    private final PwnedPasswordClient client;

    public PwnedPasswordChecker(PwnedPasswordClient client) {
        this.client = client;
    }

    public boolean check(String password) {
        org.pwnedpasswords4j.client.Hex hashedPassword = hashPassword(password);
        List<org.pwnedpasswords4j.client.Hex> hashes = client.fetchHashes(hashedPassword);

        return hashes.contains(hashedPassword);
    }

    private org.pwnedpasswords4j.client.Hex hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] digest = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            return org.pwnedpasswords4j.client.Hex.from(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static PwnedPasswordChecker standalone(String userAgent) {
        return standalone("https://api.pwnedpasswords.com/range/", userAgent);
    }

    public static PwnedPasswordChecker standalone(String url, String userAgent) {
        return new PwnedPasswordChecker(new PwnedPasswordClient(httpClient, url, userAgent));
    }

}
