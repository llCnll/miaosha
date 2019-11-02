package cn.chennan.miaosha.rabbitmq;

import cn.chennan.miaosha.domain.MiaoshaOrder;
import cn.chennan.miaosha.domain.MiaoshaUser;
import cn.chennan.miaosha.domain.OrderInfo;
import cn.chennan.miaosha.redis.RedisService;
import cn.chennan.miaosha.service.GoodsService;
import cn.chennan.miaosha.service.MiaoshaService;
import cn.chennan.miaosha.service.OrderService;
import cn.chennan.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ChenNan
 * @date 2019-10-31 下午3:33
 **/
@Slf4j
@Service
public class MQReceiver {
    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private RedisService redisService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receviceMiaosha(String message){
        log.info("receive message: "+ message);
        MiaoshaMessage mm = RedisService.stringTnBean(message, MiaoshaMessage.class);
        MiaoshaUser user = mm.getUser();
        Long goodsId = mm.getGoodsId();

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        //判断库存
        if(goods.getStockCount() < 0){
            return ;
        }
        //判断是否秒杀过
        MiaoshaOrder miaoshaOrder = orderService.getMiaoShaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(miaoshaOrder != null){
            log.info(user.getId()+"重复秒杀");
            return ;
        }
        //减库存, 下订单, 秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        if(orderInfo == null){
            return ;
        }
        log.info(user.getId()+"秒杀成功");

    }

    @RabbitListener(queues = MQConfig.QUEUE)
    public void recevice(String message){
        log.info("receive message: "+ message);
    }
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receviceTopic1(String message){
        log.info("receiveTopic1 message: "+ message);
    }
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receviceTopic2(String message){
        log.info("receiveTopic2 message: "+ message);
    }
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receviceFanout1(String message){
        log.info("receiveTopic1 message: "+ message);
    }
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receviceFanout2(String message){
        log.info("receiveTopic2 message: "+ message);
    }
    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void receviceHeader(byte[] message){
        log.info("receive Header message: "+ new String(message));
    }
}
