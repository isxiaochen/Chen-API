package com.yupi.springbootinit.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 手机令牌桶算法工具类
 */
@Slf4j
public class LeakyBucket {

    //令牌桶容量上限
    public final int tokenCapacity;

    //服务端生产令牌的速率 等价于服务端固定的速率生成令牌
    private final int rate;

    //令牌桶现在拥有的令牌数量，令牌数量是可以积累的，最大值是令牌桶容量
    public long nowTokens;


    //手机短信接口令牌桶对象，单例对象      登录令牌桶
    public static LeakyBucket loginLeakyBucket = new LeakyBucket(1,1);
    //注册令牌桶
    public static LeakyBucket registerLeakyBucket = new LeakyBucket(1,1);



    public LeakyBucket(int tokenCapacity, int rate) {
        this.tokenCapacity = tokenCapacity;
        this.rate = rate;
    }



    public boolean control(long lastTime) {

        //每60s生产一个令牌，且桶的容量是1。因为每个人60s内就只能发送一次短信
        long nowTime = System.currentTimeMillis()/1000;
        if ((nowTime-lastTime)>=60){
            nowTokens =  nowTokens + (long) rate;
        }

        //服务端生产的令牌是可以积累的，但积累的上限是桶的容量
        long tokens = Math.min(tokenCapacity, nowTokens);
        //只要令牌桶中有令牌，说明令牌在消费者能力范围内
        if (tokens>0){
            tokens--;
            //记录令牌桶剩余令牌数量
            nowTokens = tokens;
            log.info("剩余令牌桶数量："+nowTokens);
            return true;
        }else {
            return false;
        }
    }
}