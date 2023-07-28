package com.czq.apithirdpartyservices.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

import static com.czq.apicommon.constant.RabbitmqConstant.ORDER_EXCHANGE_NAME;
import static com.czq.apicommon.constant.RabbitmqConstant.ORDER_SUCCESS_EXCHANGE_ROUTING_KEY;
import static com.czq.apicommon.constant.RedisConstant.SEND_ORDER_PAY_SUCCESS_INFO;



/**

 */
@Component
@Slf4j
public class OrderPaySuccessMqUtils {
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    private String finalId = null;





    /**
     * @param outTradeNo 我们自己的订单号
     */
    public void sendOrderPaySuccess(String outTradeNo){

        finalId = outTradeNo;
        redisTemplate.opsForValue().set(SEND_ORDER_PAY_SUCCESS_INFO+outTradeNo,outTradeNo);
        String finalMessageId = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(ORDER_EXCHANGE_NAME,ORDER_SUCCESS_EXCHANGE_ROUTING_KEY,outTradeNo, message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            //生成全局唯一id
            messageProperties.setMessageId(finalMessageId);
            messageProperties.setContentEncoding("utf-8");
            return message;
        });
        log.info("消息队列给订单服务发送支付成功消息，订单好："+outTradeNo);
    }

}
