package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.czq.apicommon.entity.InterfaceInfo;
import com.czq.apicommon.entity.UserInterfaceInfo;
import com.yupi.springbootinit.annotation.AuthCheck;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.mapper.UserInterfaceInfoMapper;
import com.yupi.springbootinit.model.vo.InterfaceInfoVo;
import com.yupi.springbootinit.model.vo.UserInterfaceInfoAnalysisVo;
import com.yupi.springbootinit.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;


    @GetMapping("top/interface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceInfoVo>> listTopInterfaceInfo(){



        List<UserInterfaceInfoAnalysisVo> interfaceInfoVoList = userInterfaceInfoMapper.listTopInterfaceInfo(3);

        if (CollectionUtils.isEmpty(interfaceInfoVoList)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        List<Long> interfaceInfoIds = interfaceInfoVoList.stream()
                .map(UserInterfaceInfo::getInterfaceInfoId).collect(Collectors.toList());


        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",interfaceInfoIds);

        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);

        List<InterfaceInfoVo> infoVoList = new ArrayList<>(interfaceInfoList.size());

        for (int i = 0; i < interfaceInfoList.size(); i++) {
            InterfaceInfo interfaceInfo = interfaceInfoList.get(i);
            UserInterfaceInfoAnalysisVo userInterfaceInfoVo = interfaceInfoVoList.get(i);
            InterfaceInfoVo interfaceInfoVo = new InterfaceInfoVo();
            BeanUtils.copyProperties(interfaceInfo,interfaceInfoVo);
            interfaceInfoVo.setTotalNum(userInterfaceInfoVo.getSumNum());
            infoVoList.add(interfaceInfoVo);
        }

        return ResultUtils.success(infoVoList);
    }
}
