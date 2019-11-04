package cn.chennan.miaosha.controller;

import cn.chennan.miaosha.domain.MiaoshaUser;
import cn.chennan.miaosha.domain.OrderInfo;
import cn.chennan.miaosha.interceptor.Login;
import cn.chennan.miaosha.redis.RedisService;
import cn.chennan.miaosha.result.CodeMsg;
import cn.chennan.miaosha.result.Result;
import cn.chennan.miaosha.service.GoodsService;
import cn.chennan.miaosha.service.OrderService;
import cn.chennan.miaosha.vo.GoodsVo;
import cn.chennan.miaosha.vo.OrderDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("order")
public class OrderController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/detail")
    @ResponseBody
    @Login
    public Result<OrderDetailVo> toList(Model model, MiaoshaUser user, @RequestParam("orderId") long  orderId) {
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);

        if(order== null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        Long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setGoods(goods);
        vo.setOrder(order);
        return Result.success(vo);
    }
}
