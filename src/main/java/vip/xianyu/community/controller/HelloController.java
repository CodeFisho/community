package vip.xianyu.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
