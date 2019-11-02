package cn.chennan.miaosha.service;

import cn.chennan.miaosha.domain.MiaoshaOrder;
import cn.chennan.miaosha.domain.MiaoshaUser;
import cn.chennan.miaosha.domain.OrderInfo;
import cn.chennan.miaosha.redis.GoodsKey;
import cn.chennan.miaosha.redis.RedisService;
import cn.chennan.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ChenNan
 * @date 2019-10-25 下午5:12
 **/
@Service
public class MiaoshaService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {

        //减库存
        int ret =  goodsService.reduceStock(goods);
        if(ret > 0){
            return orderService.createOrder(user, goods);
        }
        return null;
    }

    public Long getMiaoshaResult(Long userId, Long goodsId) {

        MiaoshaOrder order = orderService.getMiaoShaOrderByUserIdGoodsId(userId, goodsId);

        if(order != null){
            return order.getOrderId();
        }else {
            Long result = redisService.get(GoodsKey.getMiaoshaGoodsStock, "" + goodsId, long.class);
            if (result > 0) {
                return 0L;
            } else {
                return -1L;
            }
        }
    }
}
