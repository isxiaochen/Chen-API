package com.czq.yuapigateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.czq.apicommon.constant.RabbitmqConstant.*;


/**
 * 接口数据一致性补偿消息相关队列定义
 */
@Configuration
@Slf4j
public class RabbitmqConfig {


    //声明交换机
    @Bean(EXCHANGE_INTERFACE_CONSISTENT)
    public Exchange EXCHANGE_INTERFACE_CONSISTENT(){
        return new DirectExchange(EXCHANGE_INTERFACE_CONSISTENT,true,false);
    }

    //声明QUEUE_LOGIN_SMS队列
    @Bean(QUEUE_INTERFACE_CONSISTENT)
    public Queue QUEUE_INTERFACE_SMS(){
        return new Queue(QUEUE_INTERFACE_CONSISTENT,true,false,false);
    }

    //交换机绑定队列
    @Bean
    public Binding BINDING_QUEUE_INTERFACE_CONSISTENT(){
        return new Binding(QUEUE_INTERFACE_CONSISTENT,
                Binding.DestinationType.QUEUE, EXCHANGE_INTERFACE_CONSISTENT,
                ROUTING_KEY_INTERFACE_CONSISTENT,null);
    }
}