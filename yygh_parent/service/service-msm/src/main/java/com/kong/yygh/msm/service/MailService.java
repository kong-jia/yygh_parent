package com.kong.yygh.msm.service;

import com.kong.yygh.vo.msm.MsmVo;
import com.kong.yygh.vo.user.LoginVo;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface MailService {
    /**
     * 给前端输入的邮箱，发送验证码
     * @param email
     * @param session
     * @return
     */
    public boolean sendMimeMail(String email, String code, HttpSession session);
    boolean send(MsmVo msmVo,HttpSession session);
}
