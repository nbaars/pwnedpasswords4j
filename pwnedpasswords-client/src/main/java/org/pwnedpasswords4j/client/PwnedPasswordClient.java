package org.pwnedpasswords4j.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.getProperty;

public class PwnedPasswordClient {

    private final OkHttpClient httpClient;
    private final String url;
    private final String userAgent;

    public PwnedPasswordClient(OkHttpClient httpClient, String url, String userAgent) {
        this.httpClient = httpClient;
        this.url = url;
        this.userAgent = userAgent;
    }

    List<org.pwnedpasswords4j.client.Hex> fetchHashes(org.pwnedpasswords4j.client.Hex hashedPassword) {
        org.pwnedpasswords4j.client.Hex range = hashedPassword.firstFive();
        Request request = new Request.Builder()
                .url(url + "/" + range)
                .header("User-Agent", userAgent)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Call failed to " + url + " got " + response);
            }
            List<org.pwnedpasswords4j.client.Hex> hashes = new ArrayList<>();
            String[] lines = response.body().string().split(getProperty("line.separator"));

            for (String line : lines) {
                hashes.add(org.pwnedpasswords4j.client.Hex.from(range + line.substring(0, line.lastIndexOf(":"))));
            }
            return hashes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        List<org.pwnedpasswords4j.client.Hex> hashes = new PwnedPasswordClient(
                new OkHttpClient(), "https://api.pwnedpasswords.com/range/", "PwnedPasswordClient for Java"
        ).fetchHashes(org.pwnedpasswords4j.client.Hex.from("5baa6"));

        hashes.forEach(System.out::println);
    }
}
