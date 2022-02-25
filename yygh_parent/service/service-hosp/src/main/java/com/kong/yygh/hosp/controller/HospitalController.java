package com.kong.yygh.hosp.controller;

import com.kong.yygh.task.result.Result;
import com.kong.yygh.hosp.service.HospitalService;
import com.kong.yygh.model.hosp.Hospital;
import com.kong.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/hospital")
//@CrossOrigin
@Api(tags = "医院管理")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;
    @GetMapping("list/{page}/{limit}")
    public Result listHosp(@PathVariable Integer page,
                           @PathVariable Integer limit,
                           HospitalQueryVo hospitalSetQueryVo){
        Page<Hospital> hospPage = hospitalService.selectHospPage(page,limit,hospitalSetQueryVo);
        return Result.ok(hospPage);
    }
    @ApiOperation(value = "更新医院上线状态")
    @GetMapping("updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable String id,
                                   @PathVariable Integer status){
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }
    @ApiOperation(value = "医院的详情信息")
    @GetMapping("showHospDetail/{id}")
    public Result showHospDetail(@PathVariable String id){
        Map<String, Object> map = hospitalService.getHospById(id);
        return Result.ok(map);
    }


}
