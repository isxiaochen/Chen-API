package com.yupi.springbootinit.model.dto.userinterface;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建请求
 *
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {


    /**
     * 主键id
     */
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

}