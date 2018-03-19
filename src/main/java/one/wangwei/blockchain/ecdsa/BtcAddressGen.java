package one.wangwei.blockchain.ecdsa;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.util.Arrays;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static java.util.Arrays.copyOfRange;

/**
 * Created by nik on 25/07/17.
 */
public class BtcAddressGen {

    public static void genBitcoinAddress(String publicKey) throws InvalidAlgorithmParameterException, NoSuchProviderException,
            NoSuchAlgorithmException, IOException {
        //Convert public key into byte array and prepend 0x04 byte to front
        byte[] pubKeyBytes = DatatypeConverter.parseHexBinary("04" + publicKey);

        //Perform sha256 hash first
        byte[] shaHashedKey = sha256Hash(pubKeyBytes);

        //Perform RIPEMD-160 hash on result
        byte[] ripemdHashedKey = ripeMD160Hash(shaHashedKey);

        //Append 0x00 as main network identifier
        ByteArrayOutputStream addrStream = new ByteArrayOutputStream();
        addrStream.write((byte) 0);
        addrStream.write(ripemdHashedKey);
        byte[] hashedKeyWithID = addrStream.toByteArray();

        //Calculate checksum
        byte[] firstSHAHash = sha256Hash(hashedKeyWithID);
        byte[] secondSHAHash = sha256Hash(firstSHAHash);
        byte[] checksum = Arrays.copyOfRange(secondSHAHash, 0, 4);

        //Add checksum to end to get binary Bitcoin address
        addrStream.write(checksum);
        byte[] binaryAddress = addrStream.toByteArray();

        //Encode in base58check and print
        System.out.println("Bitcoin address: " + encodeBase58(binaryAddress));
    }


    private static byte[] sha256Hash(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        sha256.update(input);
        return sha256.digest();
    }

    private static byte[] ripeMD160Hash(byte[] input) {
        RIPEMD160Digest ripemd160 = new RIPEMD160Digest();
        ripemd160.update(input, 0, input.length);
        byte[] output = new byte[ripemd160.getDigestSize()];
        ripemd160.doFinal(output, 0);
        return output;
    }

    private static String encodeBase58(byte[] input) {
        char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();

        int leadingZeroes = 0;
        while (leadingZeroes < input.length && input[leadingZeroes] == 0) {
            ++leadingZeroes;
        }

        //Set array size to maximum possible size
        byte[] temp = new byte[input.length * 2];
        int j = temp.length;
        int startAt = leadingZeroes;

        while (startAt < input.length) {
            byte mod = divmod58(input, startAt);
            if (input[startAt] == 0) {
                ++startAt;
            }
            temp[--j] = (byte) ALPHABET[mod];
        }

        while (j < temp.length && temp[j] == ALPHABET[0]) {
            ++j;
        }

        while (--leadingZeroes >= 0) {
            temp[--j] = (byte) ALPHABET[0];
        }

        byte[] output = copyOfRange(temp, j, temp.length);
        return new String(output);
    }

    private static byte divmod58(byte[] number, int startAt) {
        int remainder = 0;
        for (int i = startAt; i < number.length; i++) {
            int digit256 = (int) number[i] & 0xFF;
            int temp = remainder * 256 + digit256;

            number[i] = (byte) (temp / 58);

            remainder = temp % 58;
        }

        return (byte) remainder;
    }
}

