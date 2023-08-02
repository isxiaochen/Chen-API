package com.czq.apicommon.constant;

public interface RedisConstant {


    //表示redis中key对应的value存在
    String EXIST_KEY_VALUE = "1";

    //通过记录发送端成功发送的消息内容实现消息队列发送端的可靠性机制，保障发送端的消息成功路由到队列中
    String SEND_ORDER_PAY_SUCCESS_INFO = "rabbitmq:send:order:paySuccess:message:";

    //支付宝订单成功交易的记录，解决因为网络故障而多次重复收到阿里的回调通知导致的订单重复处理的问题
    String ALIPAY_TRADE_SUCCESS_RECORD = "alipay:trade:success:record:";

    //短信登录key
    public static String LOGINCODEPRE = "user::email::register::";

}
