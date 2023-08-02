package com.yupi.springbootinit.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.czq.apicommon.entity.InterfaceInfo;
import com.czq.apicommon.entity.Order;
import com.czq.apicommon.entity.UserInterfaceInfo;
import com.czq.apicommon.service.InnerOrderService;
import com.czq.apicommon.vo.OrderVO;
import com.yupi.springbootinit.annotation.AuthCheck;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.mapper.UserInterfaceInfoMapper;
import com.yupi.springbootinit.model.excel.InterfaceInfoInvokeExcel;
import com.yupi.springbootinit.model.excel.InterfaceInfoOrderExcel;
import com.yupi.springbootinit.model.vo.InterfaceInfoVo;
import com.yupi.springbootinit.model.vo.UserInterfaceInfoAnalysisVo;
import com.yupi.springbootinit.service.InterfaceInfoService;
import com.yupi.springbootinit.service.UserInterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @DubboReference
    private InnerOrderService innerOrderService;


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

    @GetMapping("/top/interface/invoke/excel")
    @AuthCheck(mustRole = "admin")
    public void topInvokeInterfaceInfoExcel(HttpServletResponse response) throws IOException {

        List<InterfaceInfoVo> interfaceInfoVOList = userInterfaceInfoService.interfaceInvokeTopAnalysis(100);
        List<InterfaceInfoInvokeExcel> collect = interfaceInfoVOList.stream().map(interfaceInfoVO -> {
            InterfaceInfoInvokeExcel interfaceInfoExcel = new InterfaceInfoInvokeExcel();
            BeanUtils.copyProperties(interfaceInfoVO, interfaceInfoExcel);
            return interfaceInfoExcel;
        }).sorted((a,b)-> b.getTotalNum() - a.getTotalNum()).collect(Collectors.toList());

        String fileName = "interface_invoke.xlsx";
        genExcel(response,fileName, InterfaceInfoInvokeExcel.class,collect);
    }

    @GetMapping("/top/interface/buy")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<OrderVO>> listTopBuyInterfaceInfo() {
        List<OrderVO> orderVOList = interfaceBuyTopAnalysis();
        return ResultUtils.success(orderVOList);
    }

    private List<OrderVO> interfaceBuyTopAnalysis() {
        List<Order> orderList = innerOrderService.listTopBuyInterfaceInfo(5);
        List<OrderVO> orderVOList = orderList.stream().map(order -> {
            Long interfaceId = order.getInterfaceId();
            InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceId);
            OrderVO orderVO = new OrderVO();
            orderVO.setInterfaceId(interfaceId);
            orderVO.setTotal(order.getCount().longValue());
            orderVO.setInterfaceName(interfaceInfo.getName());
            orderVO.setInterfaceDesc(interfaceInfo.getDescription());
            return orderVO;
        }).collect(Collectors.toList());
        return orderVOList;
    }

    @GetMapping("/top/interface/buy/excel")
    @AuthCheck(mustRole = "admin")
    public void topBuyInterfaceInfoExcel(HttpServletResponse response) throws IOException {
        List<OrderVO> orderVOList = interfaceBuyTopAnalysis();
        List<InterfaceInfoOrderExcel> collect = orderVOList.stream().map(orderVO -> {
            InterfaceInfoOrderExcel interfaceInfoOrderExcel = new InterfaceInfoOrderExcel();
            BeanUtils.copyProperties(orderVO, interfaceInfoOrderExcel);
            return interfaceInfoOrderExcel;
        }).sorted((a, b) -> (int) (b.getTotal() - a.getTotal())).collect(Collectors.toList());
        String fileName = "interface_buy.xlsx";

        genExcel(response,fileName,InterfaceInfoOrderExcel.class,collect);
    }



    private void genExcel(HttpServletResponse response,String fileName,Class entity, List collect) throws IOException {

        String sheetName = "analysis";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        // 创建ExcelWriter对象
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), entity).build();
        // 创建工作表
        WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();

        // 写入数据到工作表
        excelWriter.write(collect, writeSheet);

        // 关闭ExcelWriter对象
        excelWriter.finish();
    }

}
