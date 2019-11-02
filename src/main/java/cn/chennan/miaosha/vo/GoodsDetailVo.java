package cn.chennan.miaosha.vo;

import cn.chennan.miaosha.domain.MiaoshaUser;
import lombok.Data;

/**
 * @author ChenNan
 * @date 2019-10-31 下午12:22
 **/
@Data
public class GoodsDetailVo {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private MiaoshaUser user;
    private GoodsVo goods;
}
