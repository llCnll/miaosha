package cn.chennan.miaosha.rabbitmq;

import cn.chennan.miaosha.domain.MiaoshaUser;
import lombok.Data;

/**
 * @author ChenNan
 * @date 2019-11-02 下午2:03
 **/
@Data
public class MiaoshaMessage {
    private MiaoshaUser user;
    private Long goodsId;
}
