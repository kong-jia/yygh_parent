package com.kong.yygh.msm.service.Impl;

import com.alibaba.excel.util.StringUtils;
import com.kong.yygh.msm.service.MailService;
import com.kong.yygh.vo.msm.MsmVo;
import com.kong.yygh.vo.user.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Random;
@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;//一定要用@Autowired
    //application.properties中已配置的值
    @Value("${spring.mail.username}")
    private String from;
    @Value("spring.mail.password")
    public static String passwordKey;

    /**
     * 给前端输入的邮箱，发送验证码
     *
     * @param email
     * @param session
     * @return
     */
    @Override
    public boolean sendMimeMail(String email, String code, HttpSession session) {
        if (StringUtils.isEmpty(email)){
            return false;
        }
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setSubject("尚医通");//主题
            //生成随机数
            //将随机数放置到session中
            session.setAttribute("email", email);
            session.setAttribute("code", code);

            mailMessage.setText("您收到的验证码是：" + code+"在2分钟内有效。如非本人操作请忽略此短信");//内容

            mailMessage.setTo(email);//发给谁

            mailMessage.setFrom(from);//你自己的邮箱

            mailSender.send(mailMessage);//发送
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean sendMimeMail(String email, Map<String,Object> code, HttpSession session) {
        if (StringUtils.isEmpty(email)){
            return false;
        }
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setSubject("尚医通");//主题
            //生成随机数
            //将随机数放置到session中
            session.setAttribute("email", email);
            session.setAttribute("code", code);

            mailMessage.setText("您收到的验证码是：" + code+"在2分钟内有效。如非本人操作请忽略此短信");//内容

            mailMessage.setTo(email);//发给谁

            mailMessage.setFrom(from);//你自己的邮箱

            mailSender.send(mailMessage);//发送
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean send(MsmVo msmVo,HttpSession session) {
        if (!StringUtils.isEmpty(msmVo.getPhone())){
            boolean isSend = this.sendMimeMail(msmVo.getPhone(), msmVo.getParam(),session);
            return isSend;
        }
        return false;
    }

    /**
     * 检验验证码是否一致
     *
     * @param userVo
     * @param session
     * @return
     */
    public boolean registered(LoginVo userVo, HttpSession session) {
        //获取session中的验证信息
        String email = (String) session.getAttribute("email");
        String code = (String) session.getAttribute("code");

        //获取表单中的提交的验证信息
        String voCode = userVo.getCode();

        //如果email数据为空，或者不一致，注册失败
        if (email == null || email.isEmpty()) {
            //return "error,请重新注册";
            return false;
        } else if (!code.equals(voCode)) {
            //return "error,请重新注册";
            return false;
        }


        //跳转成功页面
        return true;
    }
}

