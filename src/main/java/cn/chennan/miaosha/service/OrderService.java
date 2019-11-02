package cn.chennan.miaosha.service;

import cn.chennan.miaosha.dao.OrderDao;
import cn.chennan.miaosha.domain.MiaoshaOrder;
import cn.chennan.miaosha.domain.MiaoshaUser;
import cn.chennan.miaosha.domain.OrderInfo;
import cn.chennan.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author ChenNan
 * @date 2019-10-25 下午5:03
 **/
@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    public MiaoshaOrder getMiaoShaOrderByUserIdGoodsId(Long userId, Long goodsId) {
        MiaoshaOrder miaoshaOrder = orderDao.getMiaoShaOrderByUserIdGoodsId(userId, goodsId);
        return miaoshaOrder;
    }

    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setUserId(user.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);


        return orderInfo;
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }
}
