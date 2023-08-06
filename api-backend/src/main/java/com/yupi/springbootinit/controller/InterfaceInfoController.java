package com.yupi.springbootinit.controller;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czq.apicommon.common.JwtUtils;
import com.czq.apicommon.entity.InterfaceCharging;
import com.czq.apicommon.entity.InterfaceInfo;
import com.czq.apicommon.entity.User;
import com.czq.apicommon.entity.UserInterfaceInfo;
import com.google.gson.Gson;
import com.yupi.springbootinit.annotation.AuthCheck;
import com.yupi.springbootinit.common.*;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.yupi.springbootinit.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.yupi.springbootinit.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.yupi.springbootinit.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.yupi.springbootinit.model.enums.InterfaceInfoStateEnum;
import com.yupi.springbootinit.model.vo.InterfaceInfoVo;
import com.yupi.springbootinit.service.InterfaceChargingService;
import com.yupi.springbootinit.service.InterfaceInfoService;
import com.yupi.springbootinit.service.UserInterfaceInfoService;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;


/**
 * API接口
 *
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;


    @Resource
    private InterfaceChargingService interfaceChargingService;

    private static final String GATEWAY_HOST="http://localhost:8090";

    private static final String RESTFUL_INTERFACE = "restful";

    private static final Gson gson = new Gson();
    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest,interfaceInfo);
        // 校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                            HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfoVo> getInterfaceInfoById(long id,HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = JwtUtils.getUserIdByToken(request);
        if (userId == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        InterfaceCharging interfaceCharging = interfaceChargingService.getOne(new QueryWrapper<InterfaceCharging>().eq("interfaceId", id));
        InterfaceInfoVo interfaceInfoVO = new InterfaceInfoVo();
        BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
        if (interfaceCharging != null) {
            //获取付费剩余调用次数
            interfaceInfoVO.setCharging(interfaceCharging.getCharging());
//            interfaceInfoVO.setAvailablePieces(interfaceCharging.getAvailablePieces());
            interfaceInfoVO.setChargingId(interfaceCharging.getId());
        }
        //获取免费剩余调用次数
        QueryWrapper<UserInterfaceInfo> userInterfaceInfoQueryWrapper = new QueryWrapper<>();
        userInterfaceInfoQueryWrapper.eq("userId",userId);
        userInterfaceInfoQueryWrapper.eq("interfaceInfoId", id);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getOne(userInterfaceInfoQueryWrapper);
        if (userInterfaceInfo!=null){
            interfaceInfoVO.setAvailablePieces(userInterfaceInfo.getLeftNum().toString());
        }

        return ResultUtils.success(interfaceInfoVO);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        // content 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "content", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }


    /**
     *  发布接口
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest,
                                                     HttpServletRequest request) {
        
        //1.判断接口是否存在
        //2.判断接口是否可以调用
        //3.修改接口的状态为发布状态
        if (idRequest == null || idRequest.getId() == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();

        Object res = invokeInterfaceInfo(interfaceInfo.getSdk(), interfaceInfo.getName(), interfaceInfo.getRequestParams(), accessKey, secretKey);
        if (res == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (res.toString().contains("Error request")) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统接口内部异常");
        }

        InterfaceInfo updateInterfaceInfo = new InterfaceInfo();
        updateInterfaceInfo.setId(id);
        updateInterfaceInfo.setStatus(InterfaceInfoStateEnum.online.getValue());
        boolean result = interfaceInfoService.updateById(updateInterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     *  下线接口
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest,
                                                     HttpServletRequest request) {
        //1.判断接口是否存在
        //2.修改接口的状态为发布状态
        if (idRequest == null || idRequest.getId() == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStateEnum.offline.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     *  在线调用接口
     * @param interfaceInfoInvokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                                      HttpServletRequest request) {
        //1.判断接口是否存在
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = interfaceInfoInvokeRequest.getId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();

        //2.用户调用次数校验
        QueryWrapper<UserInterfaceInfo> userInterfaceInfoQueryWrapper = new QueryWrapper<>();
        userInterfaceInfoQueryWrapper.eq("userId", loginUser.getId());
        userInterfaceInfoQueryWrapper.eq("interfaceInfoId", id);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getOne(userInterfaceInfoQueryWrapper);
        if (userInterfaceInfo == null){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "调用次数不足！");
        }
        int leftNum = userInterfaceInfo.getLeftNum();
        if(leftNum <= 0){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "调用次数不足！");
        }

        //3.发起接口调用
        String requestParams= interfaceInfoInvokeRequest.getUserRequestParams();
        Object res = invokeInterfaceInfo(interfaceInfo.getSdk(), interfaceInfo.getName(), requestParams, accessKey, secretKey);
        if (res == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (res.toString().contains("Error request")) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "调用错误，请检查参数和接口调用次数！");
        }
        return ResultUtils.success(res);
    }

    private Object invokeInterfaceInfo(String classPath, String methodName, String userRequestParams,
                                       String accessKey, String secretKey) {
        try {
            Class<?> clientClazz = Class.forName(classPath);
            // 1. 获取构造器，参数为ak,sk
            Constructor<?> binApiClientConstructor = clientClazz.getConstructor(String.class, String.class);
            // 2. 构造出客户端
            Object apiClient =  binApiClientConstructor.newInstance(accessKey, secretKey);

            // 3. 找到要调用的方法
            Method[] methods = clientClazz.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    // 3.1 获取参数类型列表
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 0) {
                        // 如果没有参数，直接调用
                        return method.invoke(apiClient);
                    }
                    Gson gson = new Gson();
                    // 构造参数
                    Object parameter = gson.fromJson(userRequestParams, parameterTypes[0]);
                    return method.invoke(apiClient, parameter);
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "找不到调用的方法!! 请检查你的请求参数是否正确!");
        }
    }

    @GetMapping("/sdk")
    public void getSdk(HttpServletResponse response) throws IOException {
        // 获取要下载的文件
        org.springframework.core.io.Resource resource = new ClassPathResource("api-client-sdk-0.0.1.jar");
        InputStream inputStream = resource.getInputStream();

        // 设置响应头
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=api-client-sdk-0.0.1.jar");

        // 将文件内容写入响应
        try (OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
    }

}
