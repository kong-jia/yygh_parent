package com.kong.yygh.msm.controller;

import com.alibaba.excel.util.StringUtils;
import com.kong.yygh.task.result.Result;
import com.kong.yygh.msm.service.MailService;
import com.kong.yygh.msm.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/msm")
public class LoginEmail {
    @Autowired
    private MailService mailService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;



    @PostMapping("sendEmail/{email}")
    @ResponseBody
    public Result sendEmail(@PathVariable String email, HttpSession httpSession){
        String code = redisTemplate.opsForValue().get(email);
        if (!StringUtils.isEmpty(code)){
            return Result.ok();
        }
        code= RandomUtil.getSixBitRandom();
        boolean isSend = mailService.sendMimeMail(email,code, httpSession);
        if (isSend){
            redisTemplate.opsForValue().set(email,code,2, TimeUnit.MINUTES);
            return Result.ok();
        }
        return Result.fail().message("验证码发送失败");
    }
}
