package com.czq.apiorder;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static com.czq.apiorder.config.RabbitmqConfig.EXCHANGE_ORDER_PAY;
import static com.czq.apiorder.config.RabbitmqConfig.ROUTINGKEY_ORDER_PAY;

@SpringBootTest
class ApiOrderApplicationTests {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {

        String msg="我是模拟死信队列的消息";
        rabbitTemplate.convertAndSend(EXCHANGE_ORDER_PAY,ROUTINGKEY_ORDER_PAY, msg, (message) -> {
            //设置有效时间，如果消息不被消费，进入死信队列
            message.getMessageProperties().setExpiration("1000");
            return message;
        });

    }

}
