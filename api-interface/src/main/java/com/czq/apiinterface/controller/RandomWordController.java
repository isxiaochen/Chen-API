package com.czq.apiinterface.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.czq.apiinterface.entity.ImageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/random")
public class RandomWordController {

    @GetMapping("/word")
    public String getRandomWork(){
        HttpResponse response = HttpRequest.get("https://tenapi.cn/v2/yiyan")
                .execute();
        return response.body();
    }

    @GetMapping("/image")
    public String getRandomImage(){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("format","json");
        HttpResponse response = HttpRequest.post("https://tenapi.cn/v2/acg")
                .form(paramMap)
                .execute();
        String body = response.body();
        ImageResponse imageResponse = JSONUtil.toBean(body, ImageResponse.class);
        return imageResponse.getData().getUrl();
    }
}
