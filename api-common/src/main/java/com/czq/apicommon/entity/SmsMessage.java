package com.czq.apicommon.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 邮箱 && 手机短信消息对象
 */
@Data
public class SmsMessage implements Serializable {

//    /**
//     * 手机号码
//     */
//    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 短信
     */
    private String code;

    public SmsMessage(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public SmsMessage() {
    }
}
