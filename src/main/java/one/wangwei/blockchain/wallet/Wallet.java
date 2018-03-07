package one.wangwei.blockchain.wallet;

import lombok.Data;

/**
 * 钱包
 *
 * @author wangwei
 * @date 2017/03/07
 */
@Data
public class Wallet {

    private String name;
    private String address;

    public Wallet(String name, String address) {
        this.name = name;
        this.address = address;
    }



}
