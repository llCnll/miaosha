package cn.chennan.miaosha.service;

import cn.chennan.miaosha.domain.MiaoshaUser;
import cn.chennan.miaosha.domain.OrderInfo;
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

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {

        //减库存
        int ret =  goodsService.reduceStock(goods);
        if(ret > 0){
            return orderService.createOrder(user, goods);
        }
        return null;
    }
}
