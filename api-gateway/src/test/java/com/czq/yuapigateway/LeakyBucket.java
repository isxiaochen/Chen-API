package com.czq.yuapigateway;

public class LeakyBucket {

    //令牌桶容量上限
    public final long tokenCapacity;

    //服务端生产令牌的速率 等价于服务端处理令牌的能力

    //令牌桶的本质是速率控制流量，速率作为流量的兜底方案
    //当流量远大于服务端令牌生产的速度时，请求就会被大量丢弃，这就是速率兜底方案兜不住的情况
    //那么如何合理的设置生产令牌的速度呢？目前我的想法是通过监测系统观察业务系统的流量情况，最终根据业务的平均流量设定一个合理的速度

    private final int rate;

    //令牌桶现在拥有的令牌数量，令牌数量是可以积累的，最大值是令牌桶容量
    public long nowTokens;

    //上一次请求结束的时间
    public long lastTime=System.currentTimeMillis();



    public LeakyBucket(long tokenCapacity,int rate) {
        this.tokenCapacity = tokenCapacity;
        this.rate = rate;
    }



    public boolean control() throws InterruptedException {
        //令牌桶算法：服务端限流100个请求(只能同时处理100个请求)

        //1.定义一个令牌桶，并给定容量上限
        //2.获取一定时间间隔内生产令牌的数量，服务端以固定速度生产令牌，令牌可以积累，最多积累到令牌桶容量

        //生产令牌的速度能固定的吗？是固定的，因为服务处理请求的能力是固定的
        //根据v*t获取一定时间间隔(现在任务开始的时间-上次任务结束的时间)内生产的令牌量

        //3.消费令牌，手握令牌者说明请求流量在服务端正常消费能力范围内，可以消费，否则拒绝消费

        long nowTime = System.currentTimeMillis();

        //生产令牌的速度是固定的，根据业务而定，这里的nowTime-lastTime只是演示计算的方式，并没有表达速度变化的意思
//        long productTokens = (nowTime-lastTime)*rate;
//        nowTokens =  nowTokens + productTokens;

        if ((nowTime-lastTime)/1000>=1){
            nowTokens =  nowTokens + (long) rate;
        }


        //服务端生产的令牌是可以积累的，但积累的上限是桶的容量
        long tokens = Math.min(tokenCapacity, nowTokens);

        //只要令牌桶中有令牌，说明令牌在消费者能力范围内
        if (tokens>0){
            tokens--;
            //记录令牌桶剩余令牌数量
            nowTokens = tokens;
            //更新上次请求结束的时间
            lastTime = System.currentTimeMillis();
            System.out.println("剩余令牌桶数量："+nowTokens);
            return true;
        }else {
            return false;
        }
    }
}