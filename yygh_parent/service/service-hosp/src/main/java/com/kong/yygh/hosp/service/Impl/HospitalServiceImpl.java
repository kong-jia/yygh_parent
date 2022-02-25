package com.kong.yygh.hosp.service.Impl;


import com.alibaba.fastjson.JSONObject;
import com.kong.yygh.cmn.client.DictFeignClient;
import com.kong.yygh.hosp.repository.HospitalRepository;
import com.kong.yygh.hosp.service.HospitalService;
import com.kong.yygh.model.hosp.Hospital;
import com.kong.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public void save(Map<String, Object> paramMap) {
        String jsonString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(jsonString, Hospital.class);
        String hospCode=hospital.getHoscode();
        Hospital hospitalExits=hospitalRepository.getHospitalByHoscode(hospCode);
        if (hospitalExits!=null){
            hospital.setStatus(hospitalExits.getStatus());
            hospital.setCreateTime(hospitalExits.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }else {
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalSetQueryVo) {
        Pageable pageable= PageRequest.of(page-1,limit);
        ExampleMatcher exampleMatcher=ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Hospital hospital=new Hospital();
        BeanUtils.copyProperties(hospitalSetQueryVo,hospital);
        Example<Hospital> example = Example.of(hospital, exampleMatcher);
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);
        pages.getContent().stream().forEach(item->{
            this.setHospitalHosType(item);
        });
        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    @Override
    public Map<String, Object> getHospById(String id) {
        Map<String, Object> result=new HashMap<>();
        Hospital hospital = this.setHospitalHosType(hospitalRepository.findById(id).get());
        result.put("hospital", hospital);

        //单独处理更直观
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);

        return null;
    }

    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospital!=null){
            return hospital.getHosname();
        }
        return null;
    }

    @Override
    public List<Hospital> findByHosname(String hosname) {
        List<Hospital> list = hospitalRepository.findHospitalByHosnameLike(hosname);
        return list;
    }

    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String, Object> result = new HashMap<>();
        //医院详情
        Hospital hospital = this.setHospitalHosType(this.getByHoscode(hoscode));
        result.put("hospital", hospital);
        //预约规则
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }


    private Hospital setHospitalHosType(Hospital item) {
        String i=item.getHostype();
        String hostype = dictFeignClient.getName("Hostype", i);
        String provinceCode = dictFeignClient.getName(item.getProvinceCode());
        String cityCode = dictFeignClient.getName(item.getCityCode());
        String districtCode = dictFeignClient.getName(item.getDistrictCode());


        item.getParam().put("hostypeString",hostype);
        item.getParam().put("fullAddress",provinceCode+cityCode+districtCode);
        return item;
    }
    
}
