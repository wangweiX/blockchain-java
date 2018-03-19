/* 
 * Bitcoin cryptography library
 * Copyright (c) Project Nayuki
 * 
 * https://www.nayuki.io/page/bitcoin-cryptography-library
 * https://github.com/nayuki/Bitcoin-Cryptography-Library
 */

package one.wangwei.blockchain.crypto;

import static java.lang.Integer.rotateRight;
import java.util.Arrays;
import java.util.Objects;


/**
 * Computes the SHA-256 hash of an array of bytes. Not instantiable.
 */
public final class Sha256 {
	
	private static final int BLOCK_LEN = 64;  // In bytes
	
	
	
	/*---- Static functions ----*/
	
	/**
	 * Computes and returns the SHA-256 hash of the specified binary message.
	 * @param msg the message to compute the hash of
	 * @return an object representing the message's SHA-256 hash
	 * @throws NullPointerException if the message is {@code null}
	 */
	public static Sha256Hash getHash(byte[] msg) {
		return getHash(msg, INITIAL_STATE.clone(), 0);
	}
	
	
	/**
	 * Computes and returns the SHA-256 hash of the SHA-256 hash of the specified binary message.
	 * @param msg the message to compute the double hash of
	 * @return an object representing the message's double SHA-256 hash
	 * @throws NullPointerException if the message is {@code null}
	 */
	public static Sha256Hash getDoubleHash(byte[] msg) {
		return getHash(getHash(msg).toBytes());
	}
	
	
	/**
	 * Computes and returns the HMAC-SHA-256 of the specified binary key and binary message.
	 * @param key the key for the message authentication code
	 * @param msg the message for the message authentication code
	 * @return an object representing the HMAC-SHA-256 of the key and message
	 * @throws NullPointerException if the key or message is {@code null}
	 */
	public static Sha256Hash getHmac(byte[] key, byte[] msg) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(msg);
		
		// Preprocess key, creating a new byte array
		key = Arrays.copyOf(key.length <= BLOCK_LEN ? key : getHash(key).toBytes(), BLOCK_LEN);
		
		// Compute inner hash
		for (int i = 0; i < key.length; i++)
			key[i] ^= 0x36;
		int[] state = INITIAL_STATE.clone();
		compress(state, key, key.length);
		Sha256Hash innerHash = getHash(msg, state, key.length);
		
		// Compute outer hash
		for (int i = 0; i < key.length; i++)
			key[i] ^= 0x36 ^ 0x5C;
		state = INITIAL_STATE.clone();
		compress(state, key, key.length);
		return getHash(innerHash.toBytes(), state, key.length);
	}
	
	
	
	/*---- Private functions ----*/
	
	// Note: The initState array will be modified.
	private static Sha256Hash getHash(byte[] msg, int[] initState, int prefixLen) {
		// Compress whole message blocks
		Objects.requireNonNull(msg);
		int[] state = initState;
		int off = msg.length / BLOCK_LEN * BLOCK_LEN;
		compress(state, msg, off);
		
		// Final blocks, padding, and length
		byte[] block = new byte[BLOCK_LEN];
		System.arraycopy(msg, off, block, 0, msg.length - off);
		off = msg.length % block.length;
		block[off] = (byte)0x80;
		off++;
		if (off + 8 > block.length) {
			compress(state, block, block.length);
			Arrays.fill(block, (byte)0);
		}
		long len = ((long)msg.length + prefixLen) << 3;
		for (int i = 0; i < 8; i++)
			block[block.length - 1 - i] = (byte)(len >>> (i * 8));
		compress(state, block, block.length);
		
		// Int32 array to bytes in big endian
		byte[] result = new byte[state.length * 4];
		for (int i = 0; i < result.length; i++)
			result[i] = (byte)(state[i / 4] >>> ((3 - i % 4) * 8));
		return new Sha256Hash(result);
	}
	
	
	private static void compress(int[] state, byte[] blocks, int len) {
		if (len < 0 || len % BLOCK_LEN != 0)
			throw new IllegalArgumentException();
		for (int i = 0; i < len; i += BLOCK_LEN) {
			
			// Message schedule
			int[] schedule = new int[64];
			for (int j = 0; j < BLOCK_LEN; j++)
				schedule[j / 4] |= (blocks[i + j] & 0xFF) << ((3 - j % 4) * 8);
			for (int j = 16; j < 64; j++) {
				schedule[j] = schedule[j-16] + schedule[j-7]
					+ (rotateRight(schedule[j-15],  7) ^ rotateRight(schedule[j-15], 18) ^ (schedule[j-15] >>>  3))
					+ (rotateRight(schedule[j- 2], 17) ^ rotateRight(schedule[j- 2], 19) ^ (schedule[j- 2] >>> 10));
			}
			
			// The 64 rounds
			int a = state[0];
			int b = state[1];
			int c = state[2];
			int d = state[3];
			int e = state[4];
			int f = state[5];
			int g = state[6];
			int h = state[7];
			for (int j = 0; j < 64; j++) {
				int t1 = h + (rotateRight(e, 6) ^ rotateRight(e, 11) ^ rotateRight(e, 25)) + (g ^ (e & (f ^ g))) + ROUND_CONSTANTS[j] + schedule[j];
				int t2 = (rotateRight(a, 2) ^ rotateRight(a, 13) ^ rotateRight(a, 22)) + ((a & (b | c)) | (b & c));
				h = g;
				g = f;
				f = e;
				e = d + t1;
				d = c;
				c = b;
				b = a;
				a = t1 + t2;
			}
			state[0] += a;
			state[1] += b;
			state[2] += c;
			state[3] += d;
			state[4] += e;
			state[5] += f;
			state[6] += g;
			state[7] += h;
		}
	}
	
	
	/*---- Class constants ----*/
	
	private static final int[] INITIAL_STATE = {
		0x6A09E667, 0xBB67AE85, 0x3C6EF372, 0xA54FF53A,
		0x510E527F, 0x9B05688C, 0x1F83D9AB, 0x5BE0CD19,
	};
	
	
	private static final int[] ROUND_CONSTANTS = {
		0x428A2F98, 0x71374491, 0xB5C0FBCF, 0xE9B5DBA5,
		0x3956C25B, 0x59F111F1, 0x923F82A4, 0xAB1C5ED5,
		0xD807AA98, 0x12835B01, 0x243185BE, 0x550C7DC3,
		0x72BE5D74, 0x80DEB1FE, 0x9BDC06A7, 0xC19BF174,
		0xE49B69C1, 0xEFBE4786, 0x0FC19DC6, 0x240CA1CC,
		0x2DE92C6F, 0x4A7484AA, 0x5CB0A9DC, 0x76F988DA,
		0x983E5152, 0xA831C66D, 0xB00327C8, 0xBF597FC7,
		0xC6E00BF3, 0xD5A79147, 0x06CA6351, 0x14292967,
		0x27B70A85, 0x2E1B2138, 0x4D2C6DFC, 0x53380D13,
		0x650A7354, 0x766A0ABB, 0x81C2C92E, 0x92722C85,
		0xA2BFE8A1, 0xA81A664B, 0xC24B8B70, 0xC76C51A3,
		0xD192E819, 0xD6990624, 0xF40E3585, 0x106AA070,
		0x19A4C116, 0x1E376C08, 0x2748774C, 0x34B0BCB5,
		0x391C0CB3, 0x4ED8AA4A, 0x5B9CCA4F, 0x682E6FF3,
		0x748F82EE, 0x78A5636F, 0x84C87814, 0x8CC70208,
		0x90BEFFFA, 0xA4506CEB, 0xBEF9A3F7, 0xC67178F2,
	};
	
	
	
	/*---- Miscellaneous ----*/
	
	private Sha256() {}  // Not instantiable
	
}
