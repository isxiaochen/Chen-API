package com.czq.apiinterface.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.czq.apiinterface.entity.ImageResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/day")
public class DayController {

    @PostMapping("/wallpaper")
    public String getDayWallpaperUrl(){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("format","json");
        HttpResponse response = HttpRequest.post("https://tenapi.cn/v2/bing")
                .form(paramMap)
                .execute();
        String body = response.body();
        ImageResponse imageResponse = JSONUtil.toBean(body, ImageResponse.class);
        return imageResponse.getData().getUrl();
    }
}
