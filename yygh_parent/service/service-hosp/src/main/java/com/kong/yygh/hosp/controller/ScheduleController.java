package com.kong.yygh.hosp.controller;

import com.kong.yygh.task.result.Result;
import com.kong.yygh.hosp.service.ScheduleService;
import com.kong.yygh.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/schedule")
@Api(tags = "排班设置管理")
//@CrossOrigin
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;
    @ApiOperation(value = "查询排班规则")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable long page,
                                  @PathVariable long limit,
                                  @PathVariable String hoscode,
                                  @PathVariable String depcode){
        Map<String,Object> map= scheduleService.getRuleSchedule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }
    @ApiOperation(value = "查询排班规则")
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hoscode,
                                    @PathVariable String depcode,
                                    @PathVariable String workDate){
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode,depcode,workDate);
        return Result.ok(list);
    }

}
