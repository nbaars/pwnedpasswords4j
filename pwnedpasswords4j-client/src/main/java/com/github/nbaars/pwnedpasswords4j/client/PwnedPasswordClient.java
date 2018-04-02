package com.github.nbaars.pwnedpasswords4j.client;

import okhttp3.*;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    CompletableFuture<List<Hex>> fetchHashesAsync(Hex hashedPassword) {
        Call call = httpClient.newCall(buildRequest(hashedPassword));
        CompletableFuture<List<Hex>> future = new CompletableFuture<>();

        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (!response.isSuccessful()) {
                        future.completeExceptionally(new ConnectException("Call failed to " + url + " got " + response));
                    } else {
                        future.complete(parseResponse(hashedPassword.firstFive(), response));
                    }
                } catch (IOException e) {
                    future.completeExceptionally(e);
                } finally {
                    response.close();
                }
            }
        });
        return future;
    }

    private Request buildRequest(Hex hashedPassword) {
        Hex range = hashedPassword.firstFive();
        return new Request.Builder()
                .url(url + "/" + range)
                .header("User-Agent", userAgent)
                .build();
    }

    private List<Hex> parseResponse(Hex range, Response response) throws IOException {
        List<Hex> hashes = new ArrayList<>();
        String[] lines = response.body().string().split(getProperty("line.separator"));

        for (String line : lines) {
            hashes.add(Hex.from(range + line.substring(0, line.lastIndexOf(":"))));
        }
        return hashes;
    }
}
