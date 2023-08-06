package com.czq.apicommon.constant;

public interface RabbitmqConstant {


    /* 短信相关 */
    String QUEUE_LOGIN_SMS = "queue_sms_code";
    String EXCHANGE_SMS_INFORM ="exchange_sms_inform";
    String ROUTINGKEY_SMS ="inform.login.sms";

    /*接口数据一致性相关*/
    String QUEUE_INTERFACE_CONSISTENT = "queue_interface_consistent";
    String EXCHANGE_INTERFACE_CONSISTENT = "exchange_interface_consistent";
    String ROUTING_KEY_INTERFACE_CONSISTENT = "routing_key_interface_consistent";

    /* 订单相关 */
    String ORDER_EXCHANGE_NAME = "order.exchange";
    String ORDER_SUCCESS_QUEUE_NAME = "order.pay.success.queue";
    String ORDER_SUCCESS_EXCHANGE_ROUTING_KEY = "order.pay.success";

}
