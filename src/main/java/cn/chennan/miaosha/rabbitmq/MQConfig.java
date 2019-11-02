package cn.chennan.miaosha.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChenNan
 * @date 2019-10-31 下午3:33
 **/
@Configuration
public class MQConfig {

    public final static String QUEUE = "queue";
    public final static String HEADER_QUEUE = "header_queue";
    public final static String TOPIC_QUEUE1 = "topic.queue1";
    public final static String TOPIC_QUEUE2 = "topic.queue2";
    public final static String TOPIC_EXCHANGE = "topicExchage";
    public final static String ROUTING_KEY1 = "topic.key1";
    public final static String ROUTING_KEY2 = "topic.#";
    public final static String FANOUT_EXCHANGE = "fanoutExchange";
    public final static String HEADERS_EXCHANGE = "headersExchange";

    /**
     * Direct模式 交换机模式
     */
    @Bean
    public Queue queue(){
       return new Queue(QUEUE,true);
    }

    /**
     * Topic模式 路由器模式
     * @return
     */
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE1,true);
    }
    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE2,true);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }
    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUTING_KEY1);
    }

    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUTING_KEY2);
    }
    /**
     * Fanout模式 广播模式
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
   @Bean
   public Binding FanoutBing1(){
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
   }
    @Bean
    public Binding FanoutBing2(){
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }

    /**
     * Headers 模式
     */
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }
   @Bean
   public Queue headerQueue(){
        return new Queue(HEADER_QUEUE);
   }
   @Bean
    public Binding hearderBinding(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("header1", "value1");
        map.put("header2", "value2");
        return BindingBuilder.bind(headerQueue()).to(headersExchange()).whereAll(map).match();
   }
}
