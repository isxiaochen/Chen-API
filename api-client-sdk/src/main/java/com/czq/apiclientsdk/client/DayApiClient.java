package com.czq.apiclientsdk.client;

import cn.hutool.http.HttpRequest;

/**
 * DayController-DayApiClient
 */
public class DayApiClient extends CommonApiClient{

    public DayApiClient(String accessKey, String secretKey) {
        super(accessKey, secretKey);
    }

    /**
     * 获取每日壁纸
     * @return
     */
    public String getDayWallpaperUrl(){
        return HttpRequest.post(GATEWAY_HOST+"/api/interface/day/wallpaper")
                .addHeaders(getHeadMap("",accessKey,secretKey))
                .execute().body();
    }
}
