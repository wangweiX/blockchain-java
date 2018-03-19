package one.wangwei.blockchain.ecdsa;


import javax.xml.bind.DatatypeConverter;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.bouncycastle.jcajce.provider.digest.Keccak;

/**
 * Created by nik on 25/07/17.
 */
public class EthAddressGen {

    public static void genEthereumAddress(String publicKey) throws NoSuchAlgorithmException, NoSuchProviderException,
            InvalidAlgorithmParameterException {
        //Convert public key into a byte array to use in hash function
        byte[] publicKeyBytes = DatatypeConverter.parseHexBinary(publicKey);
        String hashedPublicKey = getAddress(publicKeyBytes);
        //Take only last 40 characters of hash as the address
        String address = hashedPublicKey.substring(hashedPublicKey.length() - 40);

        //Prepend a 0x to the address before printing
        System.out.println("Ethereum Address: 0x" + address);
    }

    private static String getAddress(byte[] publicKey) {
        //Use Keccak256 hash on public key
        Keccak.DigestKeccak keccak = new Keccak.Digest256();
        keccak.update(publicKey);
        return toHexString(keccak.digest(), 0, keccak.digest().length);
    }

    public static String toHexString(byte[] input, int offset, int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = offset; i < offset + length; i++) {
            stringBuilder.append(String.format("%02x", input[i] & 0xFF));
        }

        return stringBuilder.toString();
    }
}
