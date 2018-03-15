package org.pwnedpasswords4j.client;


import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

public class PwnedPasswordCheckerTest {


    @Test
    public void noResultsFromClientShouldNotFindPassword() {
        PwnedPasswordClient client = Mockito.mock(PwnedPasswordClient.class);
        doAnswer(invocation -> CompletableFuture.completedFuture(new ArrayList<>()))
                .when(client).fetchHashesAsync(any());

        boolean isWeakPassword = new PwnedPasswordChecker(client).check("test");

        assertFalse(isWeakPassword);
    }

    @Test
    public void resultsFromClientWithoutMatchShouldNotFindPassword() {
        PwnedPasswordClient client = Mockito.mock(PwnedPasswordClient.class);
        doAnswer(invocation -> CompletableFuture.completedFuture(Arrays.asList(Hex.from("ae"))))
                .when(client).fetchHashesAsync(any());

        boolean isWeakPassword = new PwnedPasswordChecker(client).check("test");

        assertFalse(isWeakPassword);
    }

    @Test
    public void resultsFromClientWithMatchShouldFindPassword() {
        PwnedPasswordClient client = Mockito.mock(PwnedPasswordClient.class);
        doAnswer(invocation -> CompletableFuture.completedFuture(Arrays.asList(Hex.from("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8"))))
                .when(client).fetchHashesAsync(any());

        boolean isWeakPassword = new PwnedPasswordChecker(client).check("test");

        assertFalse(isWeakPassword);
    }

    @Test
    public void multipleHashesWithPasswordShouldFindPassword() {
        PwnedPasswordClient client = Mockito.mock(PwnedPasswordClient.class);
        doAnswer(invocation -> CompletableFuture.completedFuture(Arrays.asList(Hex.from("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8"))))
                .when(client).fetchHashesAsync(any());

        boolean isWeakPassword = new PwnedPasswordChecker(client).check("test");

        assertFalse(isWeakPassword);
    }

    @Test
    public void asyncMultipleHashesWithPasswordShouldFindPassword() throws ExecutionException, InterruptedException {
        PwnedPasswordClient client = Mockito.mock(PwnedPasswordClient.class);
        doAnswer(invocation -> CompletableFuture.completedFuture(Arrays.asList(Hex.from("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8"))))
                .when(client).fetchHashesAsync(any());

        Future<Boolean> f = new PwnedPasswordChecker(client).asyncCheck("test");

        assertFalse(f.get());
    }

}