package com.kong.yygh.hosp.controller.api;

import com.alibaba.excel.util.StringUtils;
import com.kong.yygh.task.helper.HttpRequestHelper;
import com.kong.yygh.task.result.Result;
import com.kong.yygh.task.result.ResultCodeEnum;
import com.kong.yygh.task.utils.MD5;
import com.kong.yygh.task.utils.YyghException;
import com.kong.yygh.hosp.service.DepartmentService;
import com.kong.yygh.hosp.service.HospitalService;
import com.kong.yygh.hosp.service.HospitalSetService;
import com.kong.yygh.hosp.service.ScheduleService;
import com.kong.yygh.model.hosp.Department;
import com.kong.yygh.model.hosp.Hospital;
import com.kong.yygh.model.hosp.Schedule;
import com.kong.yygh.vo.hosp.DepartmentQueryVo;
import com.kong.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "上传医院")
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);
        String sign = (String)paramMap.get("sign");
        String hoscode = (String)paramMap.get("hoscode");
        String signKey=  hospitalSetService.getSignKey(hoscode);
        signKey=MD5.encrypt(signKey);
        if (!sign.equals(signKey)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoDataString = (String)paramMap.get("logoData");
        if(!StringUtils.isEmpty(logoDataString)) {
            String logoData = logoDataString.replaceAll(" ", "+");
            paramMap.put("logoData", logoData);
        }
        hospitalService.save(paramMap);
        return Result.ok();
    }

    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String)paramMap.get("hoscode");
        String sign = (String)paramMap.get("sign");
        //hospitalSetService.getSignKey(hoscode)
        String s=MD5.encrypt(hospitalSetService.getSignKey(hoscode));
        if (!sign.equals(s)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String)paramMap.get("hoscode");
        String sign = (String)paramMap.get("sign");
        //hospitalSetService.getSignKey(hoscode)
        String s=MD5.encrypt(hospitalSetService.getSignKey(hoscode));
        if (!sign.equals(s)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        departmentService.save(paramMap);
        return Result.ok();
    }
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String)paramMap.get("hoscode");
        String depcode = (String)paramMap.get("depcode");
        int page = StringUtils.isEmpty(paramMap.get("page"))
                ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit"))
                ? 1 : Integer.parseInt((String) paramMap.get("limit"));
        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        String sign = (String)paramMap.get("sign");
        //hospitalSetService.getSignKey(hoscode)
        //签名校验
        if (!sign.equals(MD5.encrypt(hospitalSetService.getSignKey(hoscode)))){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        DepartmentQueryVo departmentVo=new DepartmentQueryVo();
        departmentVo.setHoscode(hoscode);
        departmentVo.setDepcode(depcode);
        Page<Department> pageModel = departmentService.findPageDepartment(page, limit, departmentVo );
        return Result.ok(pageModel);
    }
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String)paramMap.get("hoscode");
        String depcode = (String)paramMap.get("depcode");
        departmentService.remove(hoscode,depcode);
        return Result.ok();
    }

    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);
        scheduleService.save(paramMap);
       return Result.ok();
    }

    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String)paramMap.get("hoscode");
        String depcode = (String)paramMap.get("depcode");
        int page = StringUtils.isEmpty(paramMap.get("page"))
                ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit"))
                ? 1 : Integer.parseInt((String) paramMap.get("limit"));
        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        String sign = (String)paramMap.get("sign");
        //签名校验
        if (!sign.equals(MD5.encrypt(hospitalSetService.getSignKey(hoscode)))){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        ScheduleQueryVo scheduleQueryVo=new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> pageModel = scheduleService.findPageSchedule(page, limit, scheduleQueryVo );
        return Result.ok(pageModel);
    }
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String)paramMap.get("hoscode");
        String hosScheduleId = (String)paramMap.get("hosScheduleId");
        scheduleService.remove(hoscode,hosScheduleId);
        return Result.ok();
    }
}
