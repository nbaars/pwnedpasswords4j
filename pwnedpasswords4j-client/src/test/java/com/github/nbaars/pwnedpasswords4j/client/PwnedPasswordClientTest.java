package com.github.nbaars.pwnedpasswords4j.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.google.common.base.CharMatcher;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Matchers;

import java.net.ConnectException;
import java.util.concurrent.ExecutionException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PwnedPasswordClientTest {

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(8089);

    @Rule
    public WireMockClassRule instanceRule = wireMockRule;

    @Test
    public void wrongURLShouldThrowException() {
        PwnedPasswordClient pwnedPasswordClient = new PwnedPasswordClient(new OkHttpClient(), "http://localhost:1111", "unit-test");

        try {
            pwnedPasswordClient.fetchHashesAsync(Hex.from("password")).get();
            fail();
        } catch (InterruptedException e) {
            fail();
        } catch (ExecutionException e) {
            assertTrue(e.getCause().getClass() == ConnectException.class);
        }
    }

    @Test
    public void responseIsNotFoundShouldThrowException() {
        PwnedPasswordClient pwnedPasswordClient = new PwnedPasswordClient(new OkHttpClient(), "http://localhost:8089", "unit-test");

        try {
            pwnedPasswordClient.fetchHashesAsync(Hex.from("password")).get();
            fail();
        } catch (ExecutionException e) {
            assertTrue(e.getCause().getClass() == ConnectException.class);
        } catch (InterruptedException e) {
            fail();
        }
    }

    @Test
    public void responseIsEmptyShouldReturnEmptyList() throws ExecutionException, InterruptedException {
        instanceRule.stubFor(WireMock.get(urlEqualTo("/range/5BAA6")).willReturn(aResponse().withBody("003D68EB55068C33ACE09247EE4C639306B:3\n" +
                "012C192B2F16F82EA0EB9EF18D9D539B0DD:1")));
        PwnedPasswordClient pwnedPasswordClient = new PwnedPasswordClient(new OkHttpClient(), "http://localhost:8089/range", "unit-test");

        assertThat(pwnedPasswordClient.fetchHashesAsync(Hex.from("5BAA6")).get().size(), is(2));
    }

    @Test
    public void wrongHashReturns400() {
        instanceRule.stubFor(WireMock.get(urlEqualTo("/range/5BAA6A")).willReturn(aResponse().withStatus(400)));
        PwnedPasswordClient pwnedPasswordClient = new PwnedPasswordClient(new OkHttpClient(), "http://localhost:8089/range", "unit-test");

        try {
            pwnedPasswordClient.fetchHashesAsync(Hex.from("5BAA6A")).get();
            fail();
        } catch (ExecutionException e) {
            assertTrue(e.getCause().getClass() == ConnectException.class);
        } catch (InterruptedException e) {
            fail();
        }
    }


}