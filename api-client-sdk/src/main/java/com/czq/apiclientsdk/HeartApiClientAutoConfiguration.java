package com.czq.apiclientsdk;

import com.czq.apiclientsdk.client.HeartApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("czq.api")
@ComponentScan
@Data
public class HeartApiClientAutoConfiguration {

    private String accessKey;
    private String secretKey;


    @Bean
    public HeartApiClient czqClient(){
        return new HeartApiClient(accessKey,secretKey);
    }
}
