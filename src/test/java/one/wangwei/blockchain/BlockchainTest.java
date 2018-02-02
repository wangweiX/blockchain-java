package one.wangwei.blockchain;

public class BlockchainTest {

    public static void main(String[] args) {
        Blockchain blockchain = Blockchain.newBlockchain();
        for (int i = 0; i < 10; i++) {
            blockchain.addBlock("I'm Block and my Block num is " + i);
        }

        for (Block block : blockchain.getBlockList()) {
            String previousHash = block.getPreviousHash();
            String data = block.getData();
            String hash = block.getHash();

            System.out.printf("previousHash= %s\n data= %s\n hash= %s\n\n", previousHash, data, hash);
        }
    }
}
