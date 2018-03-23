package one.wangwei.blockchain;

import one.wangwei.blockchain.wallet.Wallet;

public class ECDSATest {

    public static void main(String[] args) {

        try {

            for (int i = 0; i < 100; i++) {
                Wallet wallet = new Wallet();
                System.out.println("bitcoin address: " + wallet.getAddress());
            }


//            String plaintext = "wangwei";
//
//            ByteArrayOutputStream addrStream = new ByteArrayOutputStream();
//            addrStream.write((byte) 0);
//            addrStream.write(plaintext.getBytes());
//            byte[] versionedPayload = addrStream.toByteArray();
//
//            Security.addProvider(new BouncyCastleProvider());
//            ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("B-571");
//            // Create a KeyPairGenerator for the Elliptic Curve algorithm.
//            KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", "BC");
//
//            g.initialize(ecSpec, new SecureRandom());
//            KeyPair pair = g.generateKeyPair();
//
//            PrivateKey privateKey = pair.getPrivate();
//            PublicKey publicKey = pair.getPublic();
//
//            System.out.println(privateKey.getEncoded());
//            System.out.println(publicKey.getEncoded());
//
//            Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", "BC");
//            ecdsaSign.initSign(pair.getPrivate());
//            ecdsaSign.update(plaintext.getBytes("UTF-8"));
//            byte[] signature = ecdsaSign.sign();
//
//            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA", "BC");
//            ecdsaVerify.initVerify(pair.getPublic());
//            ecdsaVerify.update(plaintext.getBytes("UTF-8"));
//            boolean result = ecdsaVerify.verify(signature);
//
//            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
