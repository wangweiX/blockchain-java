package one.wangwei.blockchain.network.message.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.wangwei.blockchain.network.message.InvTypeEnum;

/**
 * 获取数据
 *
 * @author wangwei
 * @date 2018/08/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDataMessageData extends BaseMessageData {

    private InvTypeEnum type;
    private String Id;
}
