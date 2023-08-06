package com.czq.apicommon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInterfaceInfoMessage implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 调用接口id
     */
    private Long interfaceInfoId;
}
