package com.yupi.springbootinit.listener;

import com.czq.apicommon.vo.UserInterfaceInfoMessage;
import com.rabbitmq.client.Channel;
import com.yupi.springbootinit.service.UserInterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

import static com.czq.apicommon.constant.RabbitmqConstant.QUEUE_INTERFACE_CONSISTENT;


/**
 * 接口调用监听器，如果接口调用失败则需要回滚数据库的接口统计数据
 */
@Component
@Slf4j
public class InterfaceInvokeListener {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;


    //监听queue_sms_code队列，实现接口统计功能
    //生产者是懒加载机制，消费者是饿汉加载机制，二者机制不对应，所以消费者要自行创建队列并加载，否则会报错
    @RabbitListener(queuesToDeclare = { @Queue(QUEUE_INTERFACE_CONSISTENT)})
    public void receiveSms(UserInterfaceInfoMessage userInterfaceInfoMessage, Message message, Channel channel) throws IOException {
        log.info("监听到消息啦，内容是："+userInterfaceInfoMessage);

        Long userId = userInterfaceInfoMessage.getUserId();
        Long interfaceInfoId = userInterfaceInfoMessage.getInterfaceInfoId();

        boolean result = false;
        try {
            result = userInterfaceInfoService.recoverInvokeCount(userId, interfaceInfoId);
        } catch (Exception e) {
            log.error("接口统计数据回滚失败！！！");
            e.printStackTrace();
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
            return;
        }

        if (!result){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }

        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

    }


}