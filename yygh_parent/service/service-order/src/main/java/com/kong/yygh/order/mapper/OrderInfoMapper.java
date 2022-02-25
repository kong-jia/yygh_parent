package com.kong.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kong.yygh.model.order.OrderInfo;
import com.kong.yygh.vo.order.OrderCountQueryVo;
import com.kong.yygh.vo.order.OrderCountVo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Service;

import java.util.List;


public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    public List<OrderCountVo> selectOrderCount(@Param("vo") OrderCountQueryVo vo);
}
