package com.yupi.springbootinit.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * json序列化和反序列化工具类配置
 */
@Configuration
public class GsonConfig {


    @Bean
    public Gson gson(){
        return new Gson();
    }
}
