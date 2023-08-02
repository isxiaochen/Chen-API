package com.yupi.springbootinit.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.io.Serializable;

/**
 * @author niuma
 * @create 2023-05-04 14:11
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(25)
@ColumnWidth(25)
public class InterfaceInfoOrderExcel implements Serializable {

    /**
     * 接口id
     */
    @ExcelProperty("接口id")
    private Long interfaceId;

    /**
     * 接口名
     */
    @ExcelProperty("接口名")

    private String interfaceName;

    /**
     * 接口描述
     */
    @ExcelProperty("接口描述")
    private String interfaceDesc;

    /**
     * 购买数量
     */
    @ExcelProperty("购买数量")
    private Long total;


    private static final long serialVersionUID = 1L;

}
