package com.czq.apiclientsdk.client;

import cn.hutool.http.HttpResponse;
import com.czq.apiclientsdk.strategy.RequestStrategy;
import com.czq.apiclientsdk.strategy.RequestStrategyContext;

import java.util.Map;


public class HeartApiClient {

    private final String accessKey;
    private final String secretKey;

    public HeartApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }


    /**
     * 处理请求的公共接口
     * @param url       请求地址
     * @param method    请求方式
     * @param paramMap  请求参数
     * @param restfulObj  json请求参数
     * @return
     */
    public HttpResponse handleRequest(String url,String method,Map<String,Object> paramMap,Object restfulObj){
        RequestStrategy requestStrategy = RequestStrategyContext.getRequestStrategy(method);
        requestStrategy.setAccessKey(accessKey);
        requestStrategy.setSecretKey(secretKey);
        return requestStrategy.handleRequest(url,paramMap,restfulObj);
    }
}
