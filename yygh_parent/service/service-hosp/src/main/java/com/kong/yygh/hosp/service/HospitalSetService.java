package com.kong.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kong.yygh.model.hosp.HospitalSet;
import com.kong.yygh.vo.order.SignInfoVo;

public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKey(String hoscode);

    SignInfoVo getSignInfoVo(String hoscode);
}
