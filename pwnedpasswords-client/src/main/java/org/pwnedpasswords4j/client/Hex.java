package org.pwnedpasswords4j.client;

import java.math.BigInteger;
import java.util.Objects;

class Hex {

    private String hex;

    private Hex(String s) {
        this.hex = s.toUpperCase();
    }

    public static Hex from(byte[] b) {
        return new Hex(new BigInteger(1, b).toString(16));
    }

    public static Hex from(String s) {
        return new Hex(s);
    }

    public Hex firstFive() {
        return new Hex(hex.substring(0, 5));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hex hex1 = (Hex) o;
        return Objects.equals(hex, hex1.hex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hex);
    }

    @Override
    public String toString() {
        return hex;
    }
}
