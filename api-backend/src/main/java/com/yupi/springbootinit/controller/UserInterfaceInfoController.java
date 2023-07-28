package com.yupi.springbootinit.controller;


import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czq.apicommon.entity.InterfaceCharging;
import com.czq.apicommon.entity.User;
import com.yupi.springbootinit.annotation.AuthCheck;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.DeleteRequest;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.yupi.springbootinit.model.dto.userinterface.UpdateUserInterfaceInfoDTO;
import com.yupi.springbootinit.model.dto.userinterface.UserInterfaceInfoAddRequest;
import com.yupi.springbootinit.model.dto.userinterface.UserInterfaceInfoUpdateRequest;
import com.czq.apicommon.entity.UserInterfaceInfo;
import com.yupi.springbootinit.model.vo.UserInterfaceInfoVO;
import com.yupi.springbootinit.service.InterfaceChargingService;
import com.yupi.springbootinit.service.UserInterfaceInfoService;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口关系controlelr
 *
 */
@RestController
@RequestMapping("/userInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService  userInterfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private InterfaceChargingService interfaceChargingService;

    // region 增删改查

    /**
     * 创建
     *
     * @param  userInterfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
        if ( userInterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties( userInterfaceInfoAddRequest, userInterfaceInfo);
        // 校验
         userInterfaceInfoService.validUserInterfaceInfo( userInterfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
         userInterfaceInfo.setUserId(loginUser.getId());
        boolean result =  userInterfaceInfoService.save( userInterfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId =  userInterfaceInfo.getId();
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
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo =  userInterfaceInfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b =  userInterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param  userInterfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest,
                                            HttpServletRequest request) {
        if ( userInterfaceInfoUpdateRequest == null ||  userInterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo  userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties( userInterfaceInfoUpdateRequest,  userInterfaceInfo);
        // 参数校验
         userInterfaceInfoService.validUserInterfaceInfo( userInterfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id =  userInterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo =  userInterfaceInfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result =  userInterfaceInfoService.updateById( userInterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<UserInterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo  userInterfaceInfo =  userInterfaceInfoService.getById(id);
        return ResultUtils.success( userInterfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param  userInterfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<UserInterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest  userInterfaceInfoQueryRequest) {
        UserInterfaceInfo  userInterfaceInfoQuery = new UserInterfaceInfo();
        if ( userInterfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties( userInterfaceInfoQueryRequest,  userInterfaceInfoQuery);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>( userInterfaceInfoQuery);
        List<UserInterfaceInfo>  userInterfaceInfoList =  userInterfaceInfoService.list(queryWrapper);
        return ResultUtils.success( userInterfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param  userInterfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Page<UserInterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest  userInterfaceInfoQueryRequest, HttpServletRequest request) {
        if ( userInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo  userInterfaceInfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties( userInterfaceInfoQueryRequest,  userInterfaceInfoQuery);
        long current =  userInterfaceInfoQueryRequest.getCurrent();
        long size =  userInterfaceInfoQueryRequest.getPageSize();
        String sortField =  userInterfaceInfoQueryRequest.getSortField();
        String sortOrder =  userInterfaceInfoQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>( userInterfaceInfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo>  userInterfaceInfoPage =  userInterfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(userInterfaceInfoPage);
    }

    @GetMapping("/list/userId")
    public BaseResponse<List<UserInterfaceInfoVO>> getInterfaceInfoByUserId(@RequestParam Long userId, HttpServletRequest request) {
        List<UserInterfaceInfoVO > userInterfaceInfoVOList = userInterfaceInfoService.getInterfaceInfoByUserId(userId, request);
        return ResultUtils.success(userInterfaceInfoVOList);
    }

    /**
     * 给用户分配免费的调用次数
     * @param updateUserInterfaceInfoDTO
     * @param request
     * @return
     */
    @PostMapping("/get/free")
    public BaseResponse<Boolean> getFreeInterfaceCount(@RequestBody UpdateUserInterfaceInfoDTO updateUserInterfaceInfoDTO, HttpServletRequest request) {
        Long interfaceId = updateUserInterfaceInfoDTO.getInterfaceId();
        Long userId = updateUserInterfaceInfoDTO.getUserId();
        Long lockNum = updateUserInterfaceInfoDTO.getLockNum();
        if (interfaceId == null || userId == null || lockNum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (lockNum > 100) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您一次性获取的次数太多了");
        }
        synchronized (userId) {
            User loginUser = userService.getLoginUser(request);
            if (!userId.equals(loginUser.getId())) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
            long interfaceCharging = interfaceChargingService.count(new QueryWrapper<InterfaceCharging>().eq("interfaceId", interfaceId));
            if (interfaceCharging > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "这个是付费接口噢!");
            }
            UserInterfaceInfo one = userInterfaceInfoService.getOne(new QueryWrapper<UserInterfaceInfo>().eq("userId", userId).eq("interfaceInfoId", interfaceId));
            if (one != null && one.getLeftNum() >= 1000) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "您获取的次数太多了");
            }

            boolean b = userInterfaceInfoService.updateUserInterfaceInfo(updateUserInterfaceInfoDTO);
            return ResultUtils.success(b);
        }
    }
}
