package com.czq.yuapigateway;

import cn.hutool.core.util.RandomUtil;

/**
 * 漏桶算法学习
 */
public class LeakyBucket1 {

    private long capacity;
    private long rate;
    private long nowCapacity;
    private long lastTime = System.currentTimeMillis();

    public LeakyBucket1(long capacity, long rate, long nowCapacity) {
        this.capacity = capacity;
        this.rate = rate;
        this.nowCapacity = nowCapacity;
    }

    public boolean control() throws InterruptedException {

        //漏桶算法的本质是用桶的容量来进行流量限制(流量缓冲)，桶的容量就是其流量的缓冲区
        //当服务端不具备高并发的性能时，超出服务端能力的请求都会被缓冲在桶中

        //1.滴水的速度是随机的->请求流量是动态变化的(随机数实现)
        //2.桶的容量有其上限，对于漏桶算法来说，桶的容量就是流量的缓冲区
        //3.服务端以固定的速率去漏水，既消费请求
        //4.先漏水后加水 漏水象征服务端处理请求，加水象征服务端接收
        //5.当请求超过容量上限，既缓冲区不能再缓冲了就拒绝流量


        long nowTime = System.currentTimeMillis();
        //以固定速度漏水   nowTime-lastTime请求间的时间间隔
        Thread.sleep(5);
        long reduceWater = (nowTime-lastTime)*rate;

        //漏水，当桶的水量漏完时，水量为0
        nowCapacity = Math.max(0,nowCapacity- reduceWater);

        //加水，加水的速率是随机的
        nowCapacity = nowCapacity+ RandomUtil.randomInt(500);

        if (nowCapacity>capacity){
            System.out.println("限流了，目前桶的水量："+nowCapacity);
            return false;
        }

        System.out.println("目前桶的水量："+nowCapacity);
        return true;

    }


    public static void main(String[] args) throws InterruptedException {
        LeakyBucket1 leakyBucket1 = new LeakyBucket1(100,10,50);

        for (int i = 0; i < 10; i++) {
            leakyBucket1.control();
        }

    }
}