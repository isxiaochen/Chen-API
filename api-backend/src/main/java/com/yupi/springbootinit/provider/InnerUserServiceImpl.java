package com.yupi.springbootinit.provider;

import com.czq.apicommon.entity.User;
import com.czq.apicommon.service.InnerUserService;
import com.yupi.springbootinit.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserService userService;

    @Override
    public User getUserById(Long userId) {
        return userService.getById(userId);
    }
}
