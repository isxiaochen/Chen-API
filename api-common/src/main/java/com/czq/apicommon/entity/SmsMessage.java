package com.czq.apicommon.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 手机短信消息对象
 */
@Data
public class SmsMessage implements Serializable {

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 短信
     */
    private String code;

    public SmsMessage(String phone, String code) {
        this.phone = phone;
        this.code = code;
    }

    public SmsMessage() {
    }
}
