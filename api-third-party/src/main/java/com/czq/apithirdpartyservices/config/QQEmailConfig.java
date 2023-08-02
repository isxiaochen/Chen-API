package com.czq.apithirdpartyservices.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:email.properties")
@ConfigurationProperties(prefix = "msm")
@Configuration
public class QQEmailConfig implements InitializingBean {
    @Value("${msm.email}")
    private String email;

    @Value("${msm.host}")
    private String host;

    @Value("${msm.port}")
    private String port;

    @Value("${msm.password}")
    private String password;

    public static String EMAIL;
    public static String HOST;
    public static String PORT;
    public static String PASSWORD;

    @Override
    public void afterPropertiesSet() throws Exception {
        EMAIL = email;
        HOST = host;
        PORT = port;
        PASSWORD = password;
    }

}