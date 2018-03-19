package one.wangwei.blockchain.ecdsa;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;

/**
 * Created by nik on 24/07/17.
 */
public class ECKeyPair {

    private final String privateKey;
    private final String publicKey;

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    private ECKeyPair(BigInteger privateKey, BigInteger publicKey) {
        this.privateKey = padZeroes(privateKey.toString(16), 64);
        this.publicKey = padZeroes(publicKey.toString(16), 128);
    }

    private static ECKeyPair create(KeyPair keyPair) {
        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
        BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();

        BigInteger privateKeyVal = privateKey.getD();
        byte[] publicKeyBytes = publicKey.getQ().getEncoded(false);
        BigInteger publicKeyVal = new BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.length));

        return new ECKeyPair(privateKeyVal, publicKeyVal);
    }

    public static ECKeyPair createECKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException {
        //Add bouncy castle as key pair gen provider
        Security.addProvider(new BouncyCastleProvider());
        //Generate key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
        keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
        //Convert KeyPair to ECKeyPair, to store keys as BigIntegers
        return ECKeyPair.create(keyPairGenerator.generateKeyPair());
    }

    private static String padZeroes(String hexString, int reqLength) {
        while (hexString.length() < reqLength) {
            hexString = "0" + hexString;
        }
        return hexString;
    }

}
