package com.kong.yygh.hosp.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.kong.yygh.hosp.repository.DepartmentRepository;
import com.kong.yygh.hosp.service.DepartmentService;
import com.kong.yygh.model.hosp.Department;
import com.kong.yygh.model.hosp.Schedule;
import com.kong.yygh.vo.hosp.DepartmentQueryVo;
import com.kong.yygh.vo.hosp.DepartmentVo;
import com.kong.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        String s = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(s, Department.class);
        Department departmentExits = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(),department.getDepcode());
        if ( departmentExits!=null){
            departmentExits.setUpdateTime(new Date());
            departmentExits.setIsDeleted(0);
            departmentRepository.save(departmentExits);
        }else {
            department.setIsDeleted(0);
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());

            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentVo) {
        Pageable of = PageRequest.of(page, limit);
        Department department=new Department();
        BeanUtils.copyProperties(departmentVo,department);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        Example<Department> example=Example.of(department,exampleMatcher);
        Page<Department> all = departmentRepository.findAll(example, of);
        return all;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department!=null){
            departmentRepository.deleteById(department.getId());
        }
    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        List<DepartmentVo> list=new ArrayList<>();
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example<Department> example = Example.of(departmentQuery);
        List<Department> departmentList = departmentRepository.findAll(example);
        Map<String, List<Department>> collect = departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        for (Map.Entry<String, List<Department>>entry:collect.entrySet()) {
            String bigCode=entry.getKey();
            List<Department> department1List = entry.getValue();
            DepartmentVo departmentVo=new DepartmentVo();
            departmentVo.setDepcode(bigCode);
            departmentVo.setDepname(department1List.get(0).getBigname());
            List<DepartmentVo> voList=new ArrayList<>();
            for (Department depart:
                 department1List) {
                DepartmentVo departmentVo1=new DepartmentVo();
                departmentVo1.setDepname(depart.getDepname());
                departmentVo1.setDepcode(depart.getDepcode());
                voList.add(departmentVo1);
            }
            departmentVo.setChildren(voList);
            list.add(departmentVo);
        }
        return list;
    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department!=null){
           return department.getDepname();
        }
        return null;
    }

    @Override
    public Department getDepartment(String hoscode, String depcode) {
        return departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
    }

}
