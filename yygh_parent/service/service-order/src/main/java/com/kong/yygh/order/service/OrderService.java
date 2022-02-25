package com.kong.yygh.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kong.yygh.model.order.OrderInfo;
import com.kong.yygh.vo.order.OrderCountQueryVo;
import com.kong.yygh.vo.order.OrderQueryVo;

import java.util.Map;

public interface OrderService extends IService<OrderInfo> {
    Long saveOrder(String scheduleId, Long patientId);

    OrderInfo getOrder(String orderId);

    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    Map<String,Object> show(Long id);

    Boolean cancelOrder(Long orderId);

    void patientTips();

    Map<String,Object> getCountMap(OrderCountQueryVo orderCountQueryVo);
}
