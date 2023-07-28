package com.yupi.springbootinit.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口调用请求
 *
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {


    /**
     * 调用接口id
     */
    private Long id;


    /**
     * 用户请求参数
     */
    private String userRequestParams;

}