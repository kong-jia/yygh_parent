package com.kong.yygh.user.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kong.yygh.task.helper.JwtHelper;
import com.kong.yygh.task.result.Result;
import com.kong.yygh.task.result.ResultCodeEnum;
import com.kong.yygh.task.utils.YyghException;
import com.kong.yygh.model.user.UserInfo;
import com.kong.yygh.user.service.UserInfoService;
import com.kong.yygh.user.utils.ConstantWxPropertiesUtil;
import com.kong.yygh.user.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/ucenter/wx")
@Slf4j
public class WeixinApiController {
    @Autowired
    private UserInfoService userInfoService;
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result genQrConnect(HttpSession session){
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("appid", ConstantWxPropertiesUtil.WX_OPEN_APP_ID);
            String wxOpenRedirectUrl = ConstantWxPropertiesUtil.WX_OPEN_REDIRECT_URL;
            wxOpenRedirectUrl = URLEncoder.encode(wxOpenRedirectUrl, "utf-8");
            map.put("redirect_uri", wxOpenRedirectUrl);
            map.put("scope", "snsapi_login");
            map.put("state", System.currentTimeMillis()+"");
            return Result.ok(map);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return null;
        }

    }
    @GetMapping("callback")
    public String callback(String code, String state){
        try{
            //获取授权临时票据
            System.out.println("微信授权服务器回调。。。。。。");
            System.out.println("state = " + state);
            System.out.println("code = " + code);

            if (StringUtils.isEmpty(state) || StringUtils.isEmpty(code)) {
                log.error("非法回调请求");
                throw new YyghException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
            }

            //使用code和appid以及appscrect换取access_token
            StringBuffer baseAccessTokenUrl = new StringBuffer()
                    .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                    .append("?appid=%s")
                    .append("&secret=%s")
                    .append("&code=%s")
                    .append("&grant_type=authorization_code");

            String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                    ConstantWxPropertiesUtil.WX_OPEN_APP_ID,
                    ConstantWxPropertiesUtil.WX_OPEN_APP_SECRET,
                    code);

            try {
                accessTokenUrl = HttpClientUtils.get(accessTokenUrl);
                System.out.println("accessTokenUrl:"+accessTokenUrl);
            } catch (Exception e) {
                throw new YyghException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
            }
            System.out.println("使用code换取的access_token结果 = " + accessTokenUrl);

            JSONObject resultJson = JSONObject.parseObject(accessTokenUrl);
            if(resultJson.getString("errcode") != null){
                log.error("获取access_token失败：" + resultJson.getString("errcode") + resultJson.getString("errmsg"));
                throw new YyghException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
            }

            String accessToken = resultJson.getString("access_token");
            String openId = resultJson.getString("openid");
            log.info(accessToken);
            log.info(openId);
            UserInfo userInfo = userInfoService.selectWxInfoOpenId(openId);
            if (userInfo==null){
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openId);
                String resultUserInfo = null;
                try {
                    resultUserInfo = HttpClientUtils.get(userInfoUrl);
                } catch (Exception e) {
                    throw new YyghException(ResultCodeEnum.FETCH_USERINFO_ERROR);
                }
                System.out.println("使用access_token获取用户信息的结果 = " + resultUserInfo);
                JSONObject resultUserInfoJson = JSONObject.parseObject(resultUserInfo);
                if(resultUserInfoJson.getString("errcode") != null){
                    log.error("获取用户信息失败：" + resultUserInfoJson.getString("errcode") + resultUserInfoJson.getString("errmsg"));
                    throw new YyghException(ResultCodeEnum.FETCH_USERINFO_ERROR);
                }

                //解析用户信息
                String nickname = resultUserInfoJson.getString("nickname");
                String headimgurl = resultUserInfoJson.getString("headimgurl");

                userInfo = new UserInfo();
                userInfo.setOpenid(openId);
                userInfo.setNickName(nickname);
                userInfo.setStatus(1);
                userInfoService.save(userInfo);
            }

            //先根据openid进行数据库查询
            // UserInfo userInfo = userInfoService.getByOpenid(openId);
            // 如果没有查到用户信息,那么调用微信个人信息获取的接口
            // if(null == userInfo){
            //如果查询到个人信息，那么直接进行登录
            //使用access_token换取受保护的资源：微信的个人信息

            Map<String, String> map = new HashMap<>();
            String name = userInfo.getName();
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getNickName();
            }
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getPhone();
            }
            map.put("name", name);
            if(StringUtils.isEmpty(userInfo.getPhone())) {
                map.put("openid", userInfo.getOpenid());
            } else {
                map.put("openid", "");
            }
            String token = JwtHelper.createToken(userInfo.getId(), name);
            map.put("token", token);
            return "redirect:" +
                    ConstantWxPropertiesUtil.YYGH_BASE_URL +
                    "/weixin/callback?token="
                    +map.get("token")+"&openid="
                    +map.get("openid")+"&name="+URLEncoder.encode(map.get("name"),"utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
