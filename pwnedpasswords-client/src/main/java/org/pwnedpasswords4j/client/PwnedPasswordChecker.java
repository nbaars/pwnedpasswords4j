package org.pwnedpasswords4j.client;

import okhttp3.OkHttpClient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PwnedPasswordChecker {

    private static OkHttpClient httpClient = new OkHttpClient();
    private final PwnedPasswordClient client;

    public PwnedPasswordChecker(PwnedPasswordClient client) {
        this.client = client;
    }

    public CompletableFuture<Boolean> asyncCheck(String password) {
        Hex hashedPassword = hashPassword(password);
        return client.fetchHashesAsync(hashedPassword).thenApplyAsync(x -> x.contains(hashedPassword));
    }

    public boolean check(String password) {
        try {
            return asyncCheck(password).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private Hex hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] digest = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Hex.from(digest);
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
