package com.kong.yygh.user.config;

import com.kong.yygh.task.result.Result;
import com.kong.yygh.user.service.UserInfoService;
import com.kong.yygh.vo.user.LoginVo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Configuration
@MapperScan("com.kong.yygh.user.mapper")
public class UserConfig {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo){
       Map<String,Object> map = userInfoService.loginUser(loginVo);
       return Result.ok(map);
    }
}
