package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.yupi.springbootinit.annotation.AuthCheck;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.DeleteRequest;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.constant.UserConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.user.UserAddRequest;
import com.yupi.springbootinit.model.dto.user.UserLoginRequest;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.dto.user.UserRegisterRequest;
import com.yupi.springbootinit.model.dto.user.UserUpdateMyRequest;
import com.yupi.springbootinit.model.dto.user.UserUpdateRequest;
import com.czq.apicommon.entity.User;
import com.yupi.springbootinit.model.vo.LoginUserVO;
import com.yupi.springbootinit.model.vo.UserDevKeyVO;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.service.UserService;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 *
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;



    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            log.error("账号或密码不能为空!!!");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码不能为空!!!");
        }

        long result = userService.userRegister(userAccount, userPassword,checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/email/register")
    public BaseResponse<Long> userEmailRegister(@RequestBody UserRegisterRequest userRegisterRequest) {

        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String emailCaptcha = userRegisterRequest.getEmailCaptcha();
        String emailNum = userRegisterRequest.getEmailNum();
        if (StringUtils.isAnyBlank(emailNum,emailCaptcha)) {
            log.error("邮箱或邮箱验证码不能为空！！！");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"邮箱或邮箱验证码不能为空！！！");
        }

        long result = userService.userEmailRegister(emailNum,emailCaptcha);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request, HttpServletResponse response) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request,response);
        return ResultUtils.success(loginUserVO);
    }

//    /**
//     * 用户登录
//     *
//     * @param userLoginRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/loginBySms")
//    public BaseResponse<User> userLoginBySms(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request, HttpServletResponse response) {
//        if (userLoginRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        String phoneNum = userLoginRequest.getPhoneNum();
//        String phoneCaptcha = userLoginRequest.getPhoneCaptcha();
//        if (StringUtils.isAnyBlank(phoneNum, phoneCaptcha)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        User user = userService.userLoginBySms(phoneNum, phoneCaptcha, request, response);
//        return ResultUtils.success(user);
//    }




    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request,HttpServletResponse response) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request,response);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }


    /**
     * 创建用户
     *
     * @param userAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
            HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id, HttpServletRequest request) {
        BaseResponse<User> response = getUserById(id, request);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }


    /**
     * 分页获取用户列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserByPage(UserQueryRequest userQueryRequest, HttpServletRequest request) {
        long current = 1;
        long size = 10;
        User userQuery = new User();
        if (userQueryRequest != null) {
            BeanUtils.copyProperties(userQueryRequest, userQuery);
            userQuery.setPhone(null);
            current = userQueryRequest.getCurrent();
            size = userQueryRequest.getPageSize();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(userQuery);

        queryWrapper.like(userQueryRequest != null && StringUtils.isNotBlank(userQueryRequest.getPhoneNum()), "phone", userQueryRequest.getPhoneNum());
        queryWrapper.ge(StringUtils.isNotBlank(userQueryRequest.getCreateTime()), "createTime", userQueryRequest.getCreateTime());
        queryWrapper.ge(StringUtils.isNotBlank( userQueryRequest.getUpdateTime()), "phoneNum", userQueryRequest.getUpdateTime());

        Page<User> userPage = userService.page(new Page<>(current, size), queryWrapper);
        Page<UserVO> userVOPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVO> userVOList = userPage.getRecords().stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }


    /**
     * 分页获取用户封装列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    /**
     * 获取图形验证码
     *
     * @param request
     * @param response
     */
    @GetMapping("/getCaptcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        userService.getCaptcha(request, response);
    }

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
            HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 发送邮箱验证码(备案后会改为发送手机短信验证码)
     * @param emailNum
     * @return
     */
    @GetMapping("/smsCaptcha")
    public BaseResponse<Boolean> sendCode(@RequestParam String emailNum){
        if (StringUtils.isBlank(emailNum)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //^1[3-9]\d{9}$ 手机号正则表达式
        //^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$    邮箱正则表达式
        if (!Pattern.matches("[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", emailNum)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"邮箱格式错误!");
        }

        userService.sendCode(emailNum);
        return ResultUtils.success(true);
    }

    @PostMapping("/loginBySms")
    public BaseResponse<LoginUserVO> userLoginBySms(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request, HttpServletResponse response) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String emailNum = userLoginRequest.getEmailNum();
        String emailCode = userLoginRequest.getEmailCaptcha();
        if (StringUtils.isAnyBlank(emailNum, emailCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LoginUserVO user = userService.userLoginBySms(emailNum, emailCode, request, response);
        return ResultUtils.success(user);
    }


    @GetMapping("/key")
    public BaseResponse<UserDevKeyVO> getKey(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", loginUser.getId());
        queryWrapper.eq("userAccount", loginUser.getUserAccount());
        queryWrapper.select("accessKey", "secretKey");
        User user = userService.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        UserDevKeyVO userDevKeyVO = new UserDevKeyVO();
        userDevKeyVO.setSecretKey(user.getSecretKey());
        userDevKeyVO.setAccessKey(user.getAccessKey());
        return ResultUtils.success(userDevKeyVO);
    }

    @PostMapping("/gen/key")
    public BaseResponse<UserDevKeyVO> genKey(HttpServletRequest request) {
        UserDevKeyVO userDevKeyVO = userService.genkey(request);
        return ResultUtils.success(userDevKeyVO);
    }


}
