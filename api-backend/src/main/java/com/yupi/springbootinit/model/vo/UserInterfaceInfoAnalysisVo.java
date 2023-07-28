package com.yupi.springbootinit.model.vo;

import com.czq.apicommon.entity.UserInterfaceInfo;
import lombok.Data;

@Data
public class UserInterfaceInfoAnalysisVo extends UserInterfaceInfo {

    /**
     * 统计每个接口被用户调用的总数
     */
    private Integer sumNum;
}
