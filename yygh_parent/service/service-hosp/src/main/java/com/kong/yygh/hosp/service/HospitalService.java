package com.kong.yygh.hosp.service;

import com.kong.yygh.model.hosp.Hospital;
import com.kong.yygh.vo.hosp.HospitalQueryVo;
import com.kong.yygh.vo.hosp.HospitalSetQueryVo;
import com.kong.yygh.vo.order.SignInfoVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {

    void save(Map<String, Object> paramMap);

    Hospital getByHoscode(String hoscode);

    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalSetQueryVo);

    void updateStatus(String id, Integer status);

    Map<String, Object> getHospById(String id);

    String getHospName(String hoscodes);

    List<Hospital> findByHosname(String hosname);

    Map<String, Object> item(String hoscode);

}
