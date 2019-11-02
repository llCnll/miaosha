package cn.chennan.miaosha.vo;

import cn.chennan.miaosha.domain.OrderInfo;
import lombok.Data;

/**
 * @author ChenNan
 * @date 2019-10-31 下午1:48
 **/
@Data
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;
}
