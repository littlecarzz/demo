package com.example.demo.common.web;

import com.example.demo.common.utils.CommonUtil;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 描述：验证码Controller
 *
 * @author littlecar
 * @date 2019/9/6 16:34
 */
@Controller
public class KaptchaController {
    @Resource
    private DefaultKaptcha captchaProducer;

    @RequestMapping(value = {"/code"})
    public String index() {
        return "/code";
    }

    /**
     * 登录验证码SessionKey
     */
    public static final String LOGIN_VALIDATE_CODE = Constants.KAPTCHA_SESSION_KEY;
    /**
     * 登录验证码图片
     */
    @RequestMapping(value = {"/loginValidateCode"})
    public void loginValidateCode(HttpServletRequest request, HttpServletResponse response) throws Exception{
        CommonUtil.validateCode(request,response,captchaProducer,LOGIN_VALIDATE_CODE);
    }

    /**
     * 检查验证码是否正确
     */
    @RequestMapping("/checkLoginValidateCode")
    @ResponseBody
    public HashMap checkLoginValidateCode(HttpServletRequest request, @RequestParam("validateCode")String validateCode) {
        String loginValidateCode = request.getSession().getAttribute(LOGIN_VALIDATE_CODE).toString();
        HashMap<String,Object> map = new HashMap<String,Object>();
        if(loginValidateCode == null){
            map.put("status",null);//验证码过期
        }else if(loginValidateCode.equals(validateCode)){
            map.put("status",true);//验证码正确
        }else if(!loginValidateCode.equals(validateCode)){
            map.put("status",false);//验证码不正确
        }
        map.put("code",200);
        return map;
    }
}
