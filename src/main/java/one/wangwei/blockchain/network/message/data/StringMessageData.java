package one.wangwei.blockchain.network.message.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文本消息
 *
 * @author wangwei
 * @date 2018/08/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StringMessageData extends BaseMessageData {

    private String msg;
}
