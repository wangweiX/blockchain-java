/*
 * Bitcoin cryptography library
 * Copyright (c) Project Nayuki
 *
 * https://www.nayuki.io/page/bitcoin-cryptography-library
 * https://github.com/nayuki/Bitcoin-Cryptography-Library
 */

package one.wangwei.blockchain.crypto;

import java.util.Arrays;
import java.util.Objects;


/**
 * A 32-byte (256-bit) SHA-256 hash value. Immutable.
 * <p>Note that by Bitcoin convention, SHA-256 hash strings are serialized in byte-reversed order.
 * For example, these three lines all represent the same hash value:</p>
 * <ul>
 * <li>Bigint: 0x0102030405060708091011121314151617181920212223242526272829303132.</li>
 * <li>Byte array: {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x10,0x11,0x12,0x13,0x14,0x15,0x16,0x17,0x18,0x19,0x20,0x21,0x22,0x23,0x24,0x25,0x26,0x27,0x28,0x29,0x30,0x31,0x32}.</li>
 * <li>Hex string: "3231302928272625242322212019181716151413121110090807060504030201".</li>
 * <ul>
 *
 * @see Sha256
 */
public final class Sha256Hash implements Comparable<Sha256Hash> {

    /*---- Constants ----*/

    /**
     * The number of bytes in each SHA-256 hash value.
     */
    public static final int HASH_LENGTH = 32;



    /*---- Fields ----*/

    private final byte[] hash;



    /*---- Constructors ----*/

    /**
     * Constructs a SHA-256 hash object from the specified array of bytes.
     * The array must be 32 bytes long. All 2<sup>256</sup> possible values are valid.
     * Constant-time with respect to the specified value.
     *
     * @throws IllegalArgumentException if the array is not of length 32
     * @throws NullPointerException     if the array is {@code null}
     */
    public Sha256Hash(byte[] b) {
        Objects.requireNonNull(b);
        if (b.length != HASH_LENGTH)
            throw new IllegalArgumentException();
        hash = b.clone();
    }


    /**
     * Constructs a SHA-256 hash object from the specified hexadecimal string.
     * The string must be 64 characters long and entirely made up of hexadecimal digits.
     * All 2<sup>256</sup> possible values are valid. Not constant-time.
     *
     * @throws IllegalArgumentException if the string is not of length 64 or entirely hexadecimal digits
     * @throws NullPointerException     if the string is {@code null}
     */
    public Sha256Hash(String s) {
        Objects.requireNonNull(s);
        if (s.length() != HASH_LENGTH * 2 || !s.matches("[0-9a-fA-F]*"))
            throw new IllegalArgumentException("Invalid hash string");
        hash = new byte[HASH_LENGTH];
        for (int i = 0; i < hash.length; i++)
            hash[hash.length - 1 - i] = (byte) Integer.parseInt(s.substring(i * 2, (i + 1) * 2), 16);
    }



    /*---- Methods ----*/

    /**
     * Returns a new 32-byte array representing this hash value. Constant-time with respect to this hash value.
     *
     * @return a byte array representing this hash
     */
    public byte[] toBytes() {
        return hash.clone();
    }


    /**
     * Tests whether this hash is equal to the specified object. Returns {@code true} if and only if the
     * other object is a {@code Sha256Hash} object with the same byte array values. Not constant-time.
     *
     * @param obj the object to test equality with
     * @return whether the other object is a {@code Sha256Hash} with the same hash value
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        else if (!(obj instanceof Sha256Hash))
            return false;
        else
            return Arrays.equals(hash, ((Sha256Hash) obj).hash);
    }


    /**
     * Returns the hash code of this object. Constant-time with respect to this hash value.
     *
     * @return the hash code of this object
     */
    @Override
    public int hashCode() {
        return (hash[0] & 0xFF) | (hash[1] & 0xFF) << 8 | (hash[2] & 0xFF) << 16 | hash[3] << 24;
    }


    /**
     * Compares whether this hash is less than, equal to, or greater than the specified hash object. Not constant-time.
     * <p>The comparison is performed in byte-reversed order, which means the string representations are normally ordered.
     * This behavior corresponds to the comparison used in the proof-of-work check for block headers.</p>
     *
     * @return a negative number if {@code this < other}, zero if {@code this == other}, or a positive number if {@code this > other}
     * @throws NullPointerException if the other object is {@code null}
     */
    @Override
    public int compareTo(Sha256Hash other) {
        Objects.requireNonNull(other);
        for (int i = hash.length - 1; i >= 0; i--) {
            int temp = (hash[i] & 0xFF) - (other.hash[i] & 0xFF);
            if (temp != 0)
                return temp;
        }
        return 0;
    }


    /**
     * Returns the hexadecimal string representation of this hash, in lowercase, 64 digits long.
     * Remember that the string is byte-reversed with respect to the byte array. Not constant-time.
     *
     * @return a 64-digit hexadecimal string of this hash
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = hash.length - 1; i >= 0; i--)
            sb.append(String.format("%02x", hash[i]));
        return sb.toString();
    }

}
