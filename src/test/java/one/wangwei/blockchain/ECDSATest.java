package one.wangwei.blockchain;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;

import java.security.*;

public class ECDSATest {

    public static void main(String[] args) {

        try {

            String plaintext = "wangwei";

            Security.addProvider(new BouncyCastleProvider());
            ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("B-571");
            // Create a KeyPairGenerator for the Elliptic Curve algorithm.
            KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", "BC");

            g.initialize(ecSpec, new SecureRandom());
            KeyPair pair = g.generateKeyPair();

            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();

            System.out.println(privateKey.getEncoded());
            System.out.println(publicKey.getEncoded());

            Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", "BC");
            ecdsaSign.initSign(pair.getPrivate());
            ecdsaSign.update(plaintext.getBytes("UTF-8"));
            byte[] signature = ecdsaSign.sign();

            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA", "BC");
            ecdsaVerify.initVerify(pair.getPublic());
            ecdsaVerify.update(plaintext.getBytes("UTF-8"));
            boolean result = ecdsaVerify.verify(signature);

            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
