package one.wangwei.blockchain.pow;

import lombok.Data;

/**
 * <p> 工作量计算结果 </p>
 *
 * @author wangwei
 * @date 2018/02/04
 */
@Data
public class PowResult {
    /**
     *
     */
    private long nonce;
    /**
     *
     */
    private byte[] hash;

    public PowResult(long nonce, byte[] hash) {
        this.nonce = nonce;
        this.hash = hash;
    }
}
