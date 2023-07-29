package com.czq.apiclientsdk.strategy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

import java.util.Map;

public class RestfulRequest extends RequestStrategy{
    @Override
    public HttpResponse handleRequest(String url,Map<String, Object> paramMap, Object restfulObj) {

        String json = JSONUtil.toJsonStr(restfulObj);
        return HttpRequest.post(url)
                .addHeaders(getHeadMap(paramMap!=null? paramMap.toString():""))
                .body(json)
                .execute();
    }
}
