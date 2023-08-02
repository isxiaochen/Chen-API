package com.yupi.springbootinit.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author niuma
 * @create 2023-05-06 20:46
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(30)
@ColumnWidth(25)
public class InterfaceInfoInvokeExcel implements Serializable {
    private static final long serialVersionUID = -5504766653977176865L;
    /**
     * 主键
     */
    @ExcelProperty("接口Id")
    private Long id;

    /**
     * 名称
     */
    @ExcelProperty("接口名")
    private String name;

    /**
     * 描述
     */
    @ExcelProperty("接口描述")
    private String description;

    /**
     * 接口地址
     */
    @ExcelProperty("接口地址")
    private String url;



    /**
     * 已调用次数
     */
    @ExcelProperty("已调用次数")
    private Integer totalNum;


    /**
     * 接口状态（0-关闭，1-开启）
     */
    @ExcelProperty("接口状态（0-关闭，1-开启）")
    private Integer status;

    /**
     * 创建人
     */
    @ExcelProperty("创建人")
    private Long userId;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty("更新时间")

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

}
