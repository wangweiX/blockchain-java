package one.wangwei.blockchain;

import one.wangwei.blockchain.transaction.MerkleTree;
import org.apache.commons.codec.digest.DigestUtils;

public class MerkleTreeTest {

    public static void main(String[] args) {
        // level 1
        byte[][] bytes = {
                DigestUtils.sha256("node0"),
        };

        MerkleTree merkleTree = new MerkleTree(bytes);
        System.out.println(merkleTree);

    }
}
