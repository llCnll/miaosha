package cn.chennan.miaosha.controller;

import cn.chennan.miaosha.domain.MiaoshaOrder;
import cn.chennan.miaosha.domain.MiaoshaUser;
import cn.chennan.miaosha.domain.OrderInfo;
import cn.chennan.miaosha.result.CodeMsg;
import cn.chennan.miaosha.result.Result;
import cn.chennan.miaosha.service.GoodsService;
import cn.chennan.miaosha.service.MiaoshaService;
import cn.chennan.miaosha.service.MiaoshaUserService;
import cn.chennan.miaosha.service.OrderService;
import cn.chennan.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("miaosha")
public class MiaoshaController {

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private MiaoshaService miaoshaService;


    @RequestMapping("/do_miaosha")
    @ResponseBody
    public Result<OrderInfo> doMiaosha(Model model, MiaoshaUser user, @RequestParam("goodsId")Long goodsId){
        //雪花算法 snowflake
        //log.info(user.getId()+"cn.chennan.miaosha.controller.MiaoshaController#doMiaosha");
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        //判断库存
        if(goods.getStockCount() < 0){
            return Result.error(CodeMsg.Miao_SHA_OVER);
        }
        //判断是否秒杀过
        MiaoshaOrder miaoshaOrder = orderService.getMiaoShaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(miaoshaOrder != null){
            return Result.error(CodeMsg.Miao_SHA_REPEAT);
        }
        //减库存, 下订单, 秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        if(orderInfo == null){
            return Result.error(CodeMsg.Miao_SHA_OVER);
        }
        log.info(user.getId()+"秒杀成功");
        return Result.success(orderInfo);
    }
   /* @RequestMapping("/do_miaosha")
    @ResponseBody
    public Result<OrderInfo> doMiaosha(Model model, MiaoshaUser user, @RequestParam("goodsId")Long goodsId){
        //雪花算法 snowflake
        //log.info(user.getId()+"cn.chennan.miaosha.controller.MiaoshaController#doMiaosha");
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        //判断库存
        if(goods.getStockCount() < 0){
            return Result.error(CodeMsg.Miao_SHA_OVER);
        }
        //判断是否秒杀过
        MiaoshaOrder miaoshaOrder = orderService.getMiaoShaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(miaoshaOrder != null){
            return Result.error(CodeMsg.Miao_SHA_REPEAT);
        }
        //减库存, 下订单, 秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        if(orderInfo == null){
            return Result.error(CodeMsg.Miao_SHA_OVER);
        }
        log.info(user.getId()+"秒杀成功");
        return Result.success(orderInfo);
    }*/
    /**
     * 2000*10
     * QPS 1588 存在卖超情况
     *
     *
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
/*
    @RequestMapping("/do_miaosha")
    public String doMiaosha(Model model, MiaoshaUser user, @RequestParam("goodsId")Long goodsId){
        //雪花算法 snowflake
        log.info(user.getId()+"cn.chennan.miaosha.controller.MiaoshaController#doMiaosha");
        if(user == null){
            return "login";
        }
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);
        //判断库存
        if(goods.getStockCount() < 0){
            model.addAttribute("errmsg", CodeMsg.Miao_SHA_OVER.getMsg());
            log.info(user.getId()+"秒杀失败");
            return "miaosha_fail";
        }
       //判断是否秒杀过
        MiaoshaOrder miaoshaOrder = orderService.getMiaoShaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(miaoshaOrder != null){
            model.addAttribute("errmsg", CodeMsg.Miao_SHA_REPEAT.getMsg());
            log.info(user.getId()+"重复秒杀");
            return "miaosha_fail";
        }
       //减库存, 下订单, 秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
       */
/* if(orderInfo == null){
            model.addAttribute("errmsg", CodeMsg.Miao_SHA_REPEAT.getMsg());
            log.info(user.getId()+"秒杀失败");
            return "miaosha_fail";
        }*//*

        model.addAttribute("orderInfo", orderInfo);

        log.info(user.getId()+"秒杀成功");
        return "order_detail";
    }
*/
}
