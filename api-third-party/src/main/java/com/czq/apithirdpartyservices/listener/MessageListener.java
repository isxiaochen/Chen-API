package com.czq.apithirdpartyservices.listener;

import com.czq.apicommon.entity.SmsMessage;
import com.czq.apithirdpartyservices.utils.SendMessageOperation;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

import static com.czq.apicommon.constant.RabbitmqConstant.QUEUE_LOGIN_SMS;


@Component
@Slf4j
public class MessageListener {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    //监听queue_sms_code队列，实现接口统计功能
    //生产者是懒加载机制，消费者是饿汉加载机制，二者机制不对应，所以消费者要自行创建队列并加载，否则会报错
    @RabbitListener(queuesToDeclare = { @Queue(QUEUE_LOGIN_SMS)})
    public void receiveSms(SmsMessage smsMessage, Message message, Channel channel) throws IOException {
        log.info("监听到消息啦，内容是："+smsMessage);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

        //发送邮箱验证码
        SendMessageOperation messageOperation = new SendMessageOperation();
        String targetEmail = smsMessage.getEmail();
        messageOperation.sendMessage(targetEmail,stringRedisTemplate);

    }


}