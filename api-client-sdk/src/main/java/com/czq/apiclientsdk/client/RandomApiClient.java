package com.czq.apiclientsdk.client;

import cn.hutool.http.HttpRequest;

/**
 * RandomController-RandomApiClient
 */
public class RandomApiClient extends CommonApiClient{

    public RandomApiClient(String accessKey, String secretKey) {
        super(accessKey, secretKey);
    }

    /**
     * 获取随机文本
     * @return
     */
    public String getRandomWork(){
        return HttpRequest.get(GATEWAY_HOST+"/api/interface/random/word")
                .addHeaders(getHeadMap("",accessKey,secretKey))
                .execute().body();
    }

    /**
     * 获取随机动漫图片
     * @return
     */
    public String getRandomImageUrl(){
        return HttpRequest.post(GATEWAY_HOST+"/api/interface/random/image")
                .addHeaders(getHeadMap("",accessKey,secretKey))
                .execute().body();
    }
}
