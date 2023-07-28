package com.czq.apiorder.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czq.apicommon.entity.Order;
import com.czq.apicommon.vo.OrderVO;
import com.czq.apiorder.model.dto.OrderAddRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.czq.apiorder.model.dto.OrderQueryRequest;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface TOrderService extends IService<Order> {

    /**
     * 创建订单
     * @param orderAddRequest
     * @param request
     * @return
     */
    OrderVO addOrder(OrderAddRequest orderAddRequest,HttpServletRequest request);

    /**
     * 获取我的订单
     * @param orderQueryRequest
     * @param request
     * @return
     */
    Page<OrderVO> listPageOrder(OrderQueryRequest orderQueryRequest, HttpServletRequest request);
}
