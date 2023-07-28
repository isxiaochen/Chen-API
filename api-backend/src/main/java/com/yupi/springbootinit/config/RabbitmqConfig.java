package com.yupi.springbootinit.config;
 
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.czq.apicommon.constant.RabbitmqConstant.*;


/**
 * 声明异步发送短信所需要用到的交换机和队列
 */
@Configuration
@Slf4j
public class RabbitmqConfig {


    //声明交换机
    @Bean(EXCHANGE_SMS_INFORM)
    public Exchange EXCHANGE_SMS_INFORM(){
        return new DirectExchange(EXCHANGE_SMS_INFORM,true,false);
    }

    //声明QUEUE_LOGIN_SMS队列
    @Bean(QUEUE_LOGIN_SMS)
    public Queue QUEUE_INTERFACE_SMS(){
        return new Queue(QUEUE_LOGIN_SMS,true,false,false);
    }

    //交换机绑定队列
    @Bean
    public Binding BINDING_QUEUE_LOGIN_SMS(){
        return new Binding(QUEUE_LOGIN_SMS,
                Binding.DestinationType.QUEUE, EXCHANGE_SMS_INFORM,
                ROUTINGKEY_SMS,null);
    }
}