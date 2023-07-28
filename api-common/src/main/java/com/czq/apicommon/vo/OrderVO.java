package com.czq.apicommon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 */
@Data
public class OrderVO implements Serializable {

    /**
     * 接口id
     */
    private Long interfaceId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 接口名
     */
    private String interfaceName;

    /**
     * 接口描述
     */
    private String interfaceDesc;

    /**
     * 购买数量
     */
    private Long total;

    /**
     * 单价
     */
    private Double charging;

    /**
     * 交易金额
     */
    private Double totalAmount;

    /**
     * 交易状态【0->待付款；1->已完成；2->无效订单】
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 过期时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date expirationTime;

    private static final long serialVersionUID = 1L;

}
