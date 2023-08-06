package com.yupi.springbootinit.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czq.apicommon.entity.InterfaceInfo;
import com.czq.apicommon.entity.User;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.constant.UserConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.czq.apicommon.entity.UserInterfaceInfo;
import com.yupi.springbootinit.model.dto.userinterface.UpdateUserInterfaceInfoDTO;
import com.yupi.springbootinit.model.vo.InterfaceInfoVo;
import com.yupi.springbootinit.model.vo.UserInterfaceInfoVO;
import com.yupi.springbootinit.service.InterfaceInfoService;
import com.yupi.springbootinit.service.UserInterfaceInfoService;
import com.yupi.springbootinit.mapper.UserInterfaceInfoMapper;
import com.yupi.springbootinit.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;


    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 创建时，参数不能为空
        if (add) {
            if (userInterfaceInfo.getUserId() == null || userInterfaceInfo.getInterfaceInfoId() == null){
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
            }

            if (userInterfaceInfo.getTotalNum()<0 || userInterfaceInfo.getLeftNum()<0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口分配次数或者剩余次数不能小于o");
            }

        }


    }

    @Transactional
    @Override
    public boolean invokeCount(long userId, long interfaceInfoId) {

        //校验用户id，接口id是否合理
        //校验用户的接口剩余调用次数是否充足
        //接口总调用次数+1，剩余调用次数-1
        //考虑计数的并发安全问题如何解决

        if (userId<0 || interfaceInfoId<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户或接口不存在");
        }

        //查询调用接口详情，包括剩余次数和调用版本号
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        queryWrapper.eq("interfaceInfoId",interfaceInfoId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(queryWrapper);
        Integer version = userInterfaceInfo.getVersion();


        Integer leftNum = userInterfaceInfo.getLeftNum();
        if (leftNum<=0){
            log.error("接口剩余调用次数不足");
            return false;
        }


        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userId",userId);
        updateWrapper.eq("interfaceInfoId",interfaceInfoId);
        updateWrapper.eq("version",version);
        updateWrapper.gt("leftNum",0);
        updateWrapper.setSql("totalNum = totalNum +1,leftNum = leftNum-1,version = version+1");
        return this.update(updateWrapper);

    }

    @Override
    public boolean recoverInvokeCount(long userId, long interfaceInfoId) {
        if (userId<0 || interfaceInfoId<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户或接口不存在");
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userId",userId);
        updateWrapper.eq("interfaceInfoId",interfaceInfoId);
        updateWrapper.gt("leftNum",0);
        updateWrapper.setSql("totalNum = totalNum -1,leftNum = leftNum+1,version = version+1");
        return this.update(updateWrapper);
    }

    @Override
    public int getLeftInvokeCount(long userId, long interfaceInfoId) {
        //1.根据用户id和接口id获取用户接口关系详情对象
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        queryWrapper.eq("interfaceInfoId",interfaceInfoId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(queryWrapper);
        //2.从用户接口关系详情对象中获取剩余调用次数
        return userInterfaceInfo.getLeftNum();
    }


    @Override
    public boolean updateUserInterfaceInfo(UpdateUserInterfaceInfoDTO updateUserInterfaceInfoDTO) {
        Long userId = updateUserInterfaceInfoDTO.getUserId();
        Long interfaceId = updateUserInterfaceInfoDTO.getInterfaceId();
        Long lockNum = updateUserInterfaceInfoDTO.getLockNum();

        if(interfaceId == null || userId == null || lockNum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        UserInterfaceInfo one = this.getOne(
                new QueryWrapper<UserInterfaceInfo>()
                        .eq("userId", userId)
                        .eq("interfaceInfoId", interfaceId)
        );

        if (one != null) {
            // 说明是增加数量
            return this.update(
                    new UpdateWrapper<UserInterfaceInfo>()
                            .eq("userId", userId)
                            .eq("interfaceInfoId", interfaceId)
                            .setSql("leftNum = leftNum + " + lockNum)
            );
        } else {
            // 说明是第一次购买
            UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
            userInterfaceInfo.setUserId(userId);
            userInterfaceInfo.setInterfaceInfoId(interfaceId);
            userInterfaceInfo.setLeftNum(Math.toIntExact(lockNum));
            return this.save(userInterfaceInfo);
        }

    }

    @Override
    public List<UserInterfaceInfoVO> getInterfaceInfoByUserId(Long userId, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        // 判断用户是否有权限
        if(!loginUser.getId().equals(userId) && !loginUser.getUserRole().equals(UserConstant.ADMIN_ROLE)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 获取用户可调用接口列表
        QueryWrapper<UserInterfaceInfo> userInterfaceInfoQueryWrapper= new QueryWrapper<>();
        userInterfaceInfoQueryWrapper.eq("userId",loginUser.getId());
        List<UserInterfaceInfo> userInterfaceInfoList = this.list(userInterfaceInfoQueryWrapper);

        Map<Long, List<UserInterfaceInfo>> interfaceIdUserInterfaceInfoMap = userInterfaceInfoList.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        Set<Long> interfaceIds = interfaceIdUserInterfaceInfoMap.keySet();
        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>();
        if(CollectionUtil.isEmpty(interfaceIds)){
            return new ArrayList<>();
        }
        interfaceInfoQueryWrapper.in("id",interfaceIds);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(interfaceInfoQueryWrapper);
        List<UserInterfaceInfoVO> userInterfaceInfoVOList = interfaceInfoList.stream().map(interfaceInfo -> {
            UserInterfaceInfoVO userInterfaceInfoVO = new UserInterfaceInfoVO();
            // 复制接口信息
            BeanUtils.copyProperties(interfaceInfo, userInterfaceInfoVO);
            userInterfaceInfoVO.setInterfaceStatus(Integer.valueOf(interfaceInfo.getStatus()));

            // 复制用户调用接口信息
            List<UserInterfaceInfo> userInterfaceInfos = interfaceIdUserInterfaceInfoMap.get(interfaceInfo.getId());
            UserInterfaceInfo userInterfaceInfo = userInterfaceInfos.get(0);
            BeanUtils.copyProperties(userInterfaceInfo, userInterfaceInfoVO);
            return userInterfaceInfoVO;
        }).collect(Collectors.toList());
        return userInterfaceInfoVOList;
    }

    @Override
    public List<InterfaceInfoVo> interfaceInvokeTopAnalysis(int limit) {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(limit);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfoList.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        List<InterfaceInfoVo> interfaceInfoVOList = list.stream().map(interfaceInfo -> {
            InterfaceInfoVo interfaceInfoVO = new InterfaceInfoVo();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            int totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        return interfaceInfoVOList;
    }
}




