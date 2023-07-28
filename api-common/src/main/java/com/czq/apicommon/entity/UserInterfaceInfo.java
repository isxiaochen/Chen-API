package com.czq.apicommon.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName user_interface_info
 */
@TableName(value ="user_interface_info")
@Data
public class UserInterfaceInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 调用接口id
     */
    private Long interfaceInfoId;

    /**
     * 接口的总调用次数
     */
    private Integer totalNum;

    /**
     * 接口剩余调用次数
     */
    private Integer leftNum;

    /**
     * 逻辑删除 0 删除 1 正常
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 0 禁止调用 1 允许调用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 乐观锁版本号
     */
    private Integer version;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}