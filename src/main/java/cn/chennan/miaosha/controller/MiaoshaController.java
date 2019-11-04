package cn.chennan.miaosha.controller;

import cn.chennan.miaosha.domain.MiaoshaOrder;
import cn.chennan.miaosha.domain.MiaoshaUser;
import cn.chennan.miaosha.interceptor.AccessLimit;
import cn.chennan.miaosha.interceptor.Login;
import cn.chennan.miaosha.rabbitmq.MQSender;
import cn.chennan.miaosha.rabbitmq.MiaoshaMessage;
import cn.chennan.miaosha.redis.GoodsKey;
import cn.chennan.miaosha.redis.MiaoshaKey;
import cn.chennan.miaosha.redis.RedisService;
import cn.chennan.miaosha.result.CodeMsg;
import cn.chennan.miaosha.result.Result;
import cn.chennan.miaosha.service.GoodsService;
import cn.chennan.miaosha.service.MiaoshaService;
import cn.chennan.miaosha.service.MiaoshaUserService;
import cn.chennan.miaosha.service.OrderService;
import cn.chennan.miaosha.util.MD5Util;
import cn.chennan.miaosha.util.UUIDUtil;
import cn.chennan.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("miaosha")
public class MiaoshaController implements InitializingBean {

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender sender;


    @Override
    public void afterPropertiesSet() throws Exception {
        //将秒杀列表中的库存预存到redis中
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList == null){
           return;
        }
        for (GoodsVo goodsVo : goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+ goodsVo.getId(), goodsVo.getStockCount());
        }

    }

    /**
     * 先预减库存 --> 判断是否秒杀过 --> 入队(减库存, 下订单)
     * 存在卖不玩的情况...  get GoodsKey:gs1 --> "-39992"
     *
     * 相同用户可能重复秒杀, 所以库存预减, 导致reids中的值也依旧往下减
     * QPS 2570
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @Login
    @RequestMapping("/{path}/do_miaosha")
    @ResponseBody
    public Result<Integer> doMiaosha(@PathVariable("path")String path, Model model, MiaoshaUser user, @RequestParam("goodsId")Long goodsId){
        //雪花算法 snowflake
        //log.info(user.getId()+"cn.chennan.miaosha.controller.MiaoshaController#doMiaosha");

        String str = redisService.get(MiaoshaKey.getMiaoshaPath, user.getId() + "_" + goodsId, String.class);
        if(path == null || str==null || !str.equals(path)){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if(stock < 0){
            return Result.error(CodeMsg.Miao_SHA_OVER);
        }
        //判断是否秒杀过
        MiaoshaOrder miaoshaOrder = orderService.getMiaoShaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(miaoshaOrder != null){
            log.info(user.getId()+"重复秒杀");
            return Result.error(CodeMsg.Miao_SHA_REPEAT);
        }

        MiaoshaMessage message = new MiaoshaMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);

        sender.sendMiaoshaMessage(message);

        return Result.success(0);//表示排队中
    }

    //@AccessLimit(seconds = 5, maxCount = 5)
    @Login
    @AccessLimit(seconds = 5, maxCount = 5)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> path(HttpServletRequest request, Model model, MiaoshaUser user, @RequestParam("goodsId")Long goodsId){

        String str = MD5Util.md5(UUIDUtil.uuid());
        redisService.set(MiaoshaKey.getMiaoshaPath, user.getId()+"_"+goodsId, str);

        return Result.success(str);//表示排队中
    }

    /**
     * orderId : 成功
     * -1 : 秒杀失败
     * 0 : 排队中
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @Login
    @RequestMapping("/result")
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser user, @RequestParam("goodsId")Long goodsId){
        Long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);

        return Result.success(result);
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
