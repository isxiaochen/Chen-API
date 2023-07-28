package com.czq.apiclientsdk.strategy;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpResponse;
import com.czq.apiclientsdk.utils.SignUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽象请求策略
 */
@Data
public abstract class RequestStrategy {

    private String accessKey;
    private String secretKey;

    public RequestStrategy(){

    }


    public Map<String,String> getHeadMap(String body){
        //六个参数
        Map<String,String> headMap = new HashMap<>();
        headMap.put("accessKey",accessKey);
        headMap.put("body",body);
        headMap.put("sign", SignUtils.generateSign(body,secretKey));
        headMap.put("nonce", RandomUtil.randomNumbers(5));
        //当下时间/1000，时间戳大概10位
        headMap.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
        return headMap;
    }

    /**
     * 请求处理方法
     * @param url 请求地址
     * @param paramMap  请求参数
     * @param restfulObj    json类型的请求参数
     * @return
     */
    public abstract HttpResponse handleRequest(String url,  Map<String,Object> paramMap, Object restfulObj);
}
