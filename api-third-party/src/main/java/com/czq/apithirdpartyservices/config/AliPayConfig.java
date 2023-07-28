package com.czq.apithirdpartyservices.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author niumazlb
 */
@Configuration
@PropertySource("classpath:application-alipay.properties")
@ConfigurationProperties(prefix = "alipay")
@Data
public class AliPayConfig {
    /**
     * APPID
     */
    private String appId;

    /**
     * 商户私钥, 即PKCS8格式RSA2私钥
     */
    private String privateKey;

    /**
     * 支付宝公钥
     */
    private String publicKey;

    /**
     * 服务器异步通知页面路径,需http://格式的完整路径
     */
    private String notifyUrl;

    /**
     * 页面跳转同步通知页面路径,需http://格式的完整路径
     */
    private String returnUrl;

    /**
     * 签名方式
     */
    private String signType;

    /**
     * 字符编码格式
     */
    private String charset;

    /**
     * 支付宝网关
     */
    private String gatewayUrl;

    /**
     * 日志打印地址
     */
    private String logPath;
}