package cn.chennan.miaosha.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author ChenNan
 * @date 2019-10-31 下午3:33
 **/
@Slf4j
@Service
public class MQReceiver {


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
