package com.kong.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kong.yygh.enums.PaymentTypeEnum;
import com.kong.yygh.model.order.OrderInfo;
import com.kong.yygh.model.order.PaymentInfo;

import java.util.Map;

public interface PaymentService extends IService<PaymentInfo> {
    void savePaymentInfo(OrderInfo orderInfo, Integer weixin);

    void paySuccess(String out_trade_no, Integer status, Map<String, String> resultMap);

    /**
     * 获取支付记录
     * @param orderId
     * @param paymentType
     * @return
     */
    PaymentInfo getPaymentInfo(Long orderId, Integer paymentType);

}
