package vip.xianyu.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vip.xianyu.community.util.CommunityUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author:asus
 * @Date: 2021/2/23 14:29
 * @Description:
 */
@Controller
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("/communityFirst")
    @ResponseBody
    public String hello(){
        return "hello world";
    }

    @GetMapping("cookie/set")
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        //创建cookie
        Cookie cookie=new Cookie("code", CommunityUtil.generateUUID());
        //设置cookie的生效范围
        cookie.setPath("/community/hello");
        //设置cookie的生效有效时间
        cookie.setMaxAge(60*10);
        //发送cookie
        response.addCookie(cookie);
        return "cookie already set";
    }
}
