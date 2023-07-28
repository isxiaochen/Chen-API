package com.yupi.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;


@Data
public class UserInterfaceInfoVO implements Serializable {
    private static final long serialVersionUID = -1277232101530908019L;

    private Long id;
    private Long interfaceInfoId;
    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer interfaceStatus;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 已调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0-正常，1-禁用）
     */
    private Integer status;
}
