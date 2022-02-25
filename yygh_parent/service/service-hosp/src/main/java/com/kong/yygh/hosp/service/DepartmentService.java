package com.kong.yygh.hosp.service;

import com.kong.yygh.model.hosp.Department;
import com.kong.yygh.model.hosp.Schedule;
import com.kong.yygh.vo.hosp.DepartmentQueryVo;
import com.kong.yygh.vo.hosp.DepartmentVo;
import com.kong.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    void save(Map<String, Object> paramMap);

    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentVo);

    void remove(String hoscode, String depcode);

    List<DepartmentVo> findDeptTree(String hoscode);

    String getDepName(String hoscode, String depcode);

    Department getDepartment(String hoscode, String depcode);
}
