package com.github.nbaars.pwnedpasswords4j.client;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class HexTest {

    @Test
    public void equalsHashcode() {
        Hex h1 = Hex.from("aeae");
        Hex h2 = Hex.from("aeae");

        assertEquals(h1, h1);
        assertEquals(h1, h2);
        assertEquals(h2, h1);
        assertNotEquals(h1, "aeae");
        assertEquals(h1.hashCode(), h1.hashCode());
        assertEquals(h1.hashCode(), h2.hashCode());
    }

    @Test
    public void shouldReturnFirstFive() {
        Hex h = Hex.from("aeaeaeaeae");

        assertThat(h.firstFive(), is(Hex.from("aeaea")));
        assertThat(h.firstFive().toString(), is("AEAEA"));
    }

    @Test
    public void fromByte() {
        Hex h = Hex.from("aeaeaeaeae".getBytes(StandardCharsets.UTF_8));

        assertThat(h.toString(), is("61656165616561656165"));

    }

}

