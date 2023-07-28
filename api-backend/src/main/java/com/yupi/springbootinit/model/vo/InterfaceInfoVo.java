package com.yupi.springbootinit.model.vo;

import com.czq.apicommon.entity.InterfaceInfo;
import lombok.Data;

@Data
public class InterfaceInfoVo extends InterfaceInfo {

    /**
     * 统计每个接口被用户调用的总数
     */
    private Integer totalNum;


    /**
     * 计费规则（元/条）
     */
    private Double charging;

    /**
     * 计费Id
     */
    private Long chargingId;

    /**
     * 接口剩余可调用次数
     */
    private String availablePieces;

}
