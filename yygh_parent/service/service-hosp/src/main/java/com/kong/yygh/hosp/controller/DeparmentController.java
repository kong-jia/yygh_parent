package com.kong.yygh.hosp.controller;

import com.kong.yygh.task.result.Result;
import com.kong.yygh.hosp.service.DepartmentService;
import com.kong.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hosp/deparment")
@Api(tags = "科室设置管理")
//@CrossOrigin
public class DeparmentController {
    @Autowired
    private DepartmentService departmentService;
    @ApiOperation(value = "查询科室所有列表")
    @GetMapping("getDeptList/{hoscode}")
    public Result getDeptList(@PathVariable String hoscode)
    {
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }
}
