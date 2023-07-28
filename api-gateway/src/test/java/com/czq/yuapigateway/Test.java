package com.czq.yuapigateway;

public class Test {
    public static void main(String[] args) throws InterruptedException {

        //模拟令牌桶算法限制手机短信接口为60s只能发送一次
        //令牌桶容量上限

        LeakyBucket leakyBucket = new LeakyBucket(1,1);
        Thread.sleep(1000);


        for (int i = 1; i <= 100; i++) {
            if (leakyBucket.control()) {
                System.out.println("成功消费"+i);
            }else {
                System.out.println("拒绝消费"+i);
            }
        }
    }

}