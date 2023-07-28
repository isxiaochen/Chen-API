package com.czq.apiclientsdk.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求策略环境类
 */
public class RequestStrategyContext {

    private static final Map<String,RequestStrategy> requestStrategyMap = new HashMap<>();

    static {
        requestStrategyMap.put("get",new GetStrategy());
        requestStrategyMap.put("post",new PostStrategy());
        requestStrategyMap.put("restful", new RestfulRequest());
    }


    public static RequestStrategy getRequestStrategy(String method) {
        return requestStrategyMap.get(method);
    }

    public static void  putRequestStrategy(String method,RequestStrategy requestStrategy){
        requestStrategyMap.put(method,requestStrategy);
    }




}
