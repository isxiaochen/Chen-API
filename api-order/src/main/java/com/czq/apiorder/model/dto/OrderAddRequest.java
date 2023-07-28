package com.czq.apiorder.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderAddRequest {


    /**
     * 用户id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceId;

    /**
     * 计费Id
     */
    private Long chargingId;

    /**
     * 单价
     */
    private Double charging;

    /**
     * 购买数量
     */
    private Integer count;

    /**
     * 订单应付价格
     */
    private BigDecimal totalAmount;



}
