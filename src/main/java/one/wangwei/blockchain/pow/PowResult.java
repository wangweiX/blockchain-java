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
     * 计数器
     */
    private long nonce;
    /**
     * hash值
     */
    private String hash;

    public PowResult(long nonce, String hash) {
        this.nonce = nonce;
        this.hash = hash;
    }
}
