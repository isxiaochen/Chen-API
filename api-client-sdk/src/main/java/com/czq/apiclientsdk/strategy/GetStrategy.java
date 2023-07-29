package com.czq.apiclientsdk.strategy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.util.Map;

public class GetStrategy extends RequestStrategy{
    @Override
    public HttpResponse handleRequest(String url, Map<String, Object> paramMap, Object restfulObj) {
        return HttpRequest.get(url)
                .form(paramMap)
                .addHeaders(getHeadMap(paramMap!=null? paramMap.toString():""))
                .execute();
    }
}
