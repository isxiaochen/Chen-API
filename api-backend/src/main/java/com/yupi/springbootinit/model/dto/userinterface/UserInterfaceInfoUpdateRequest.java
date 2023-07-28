package com.yupi.springbootinit.model.dto.userinterface;

import lombok.Data;

import java.io.Serializable;

/**
 * 编辑请求
 *
 */
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 分配接口的调用次数
     */
    private Integer totalNum;


    /**
     * 接口的总调用次数
     */
    private Integer leftNum;

    /**
     * 0 禁止调用 1 允许调用
     */
    private Integer status;


}