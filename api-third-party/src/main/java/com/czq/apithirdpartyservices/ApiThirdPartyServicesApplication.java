package com.czq.apithirdpartyservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class ApiThirdPartyServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiThirdPartyServicesApplication.class, args);
    }
}
