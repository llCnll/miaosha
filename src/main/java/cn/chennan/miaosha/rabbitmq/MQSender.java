package cn.chennan.miaosha.rabbitmq;

import cn.chennan.miaosha.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ChenNan
 * @date 2019-10-31 下午3:33
 **/
@Slf4j
@Service
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Object Message){
        String msg = RedisService.beanToString(Message);
        log.info("send mssage: " + msg);
       amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
    }


    public void sendTopic(Object Message){
        String msg = RedisService.beanToString(Message);
        log.info("send topic mssage: " + msg);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg+"1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg+"2");
    }
    public void sendFanout(Object Message){
        String msg = RedisService.beanToString(Message);
        log.info("send fanout mssage: " + msg);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg+"2");
    }
    public void sendHeader(Object Message){
        String msg = RedisService.beanToString(Message);
        log.info("send header mssage: " + msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("header1", "value1");
        properties.setHeader("header2", "value2");
        Message obj = new Message(msg.getBytes(), properties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
    }
}
