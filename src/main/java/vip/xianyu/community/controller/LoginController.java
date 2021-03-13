package vip.xianyu.community.controller;

import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import vip.xianyu.community.common.RespBean;
import vip.xianyu.community.entity.User;
import vip.xianyu.community.service.IUserService;
import vip.xianyu.community.util.CommunityConstant;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @Author:asus
 * @Date: 2021/3/2 22:27
 * @Description:
 */
@Controller
public class LoginController {
    private static final Logger logger= LoggerFactory.getLogger("LoginController.class");

    @Autowired
    private IUserService userService;
    @Autowired
    private Producer kaptchaProducer;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @GetMapping("/register")
    public String getRegisterPage(){
        return "/site/register";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "/site/login";
    }

    @PostMapping("/register")
    public String register(Model model,User user){
        RespBean respBean=userService.register(user);
        if(respBean.getCode()==200){
            model.addAttribute("msg",respBean.getMsg());
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }else{
            model.addAttribute("msg",respBean.getMsg());
            return "/site/register";
        }
    }
    @GetMapping("/activation/{userId}/{code}")
    public String activation(Model model,@PathVariable("userId") int userId,@PathVariable("code") String code){
        int activationResult = userService.activation(userId, code);
        if(activationResult== CommunityConstant.ACTIVATION_SUCCESS){
            model.addAttribute("msg","账号激活成功，您的账号可正常使用");
            model.addAttribute("target","/login");

        }else if(activationResult==CommunityConstant.ACTIVATION_REPEAT){
            model.addAttribute("msg","该账号已经激活");
            model.addAttribute("target","/index");
        }else{
            model.addAttribute("msg","激活失败，用户不存在或激活码错误");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }

    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpServletRequest request){
        //生成验证码
        String text=kaptchaProducer.createText();
        BufferedImage img=kaptchaProducer.createImage(text);
        //存入session
        HttpSession session=request.getSession();
        session.setAttribute("kaptcha",text);

        response.setContentType("image/png");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(img,"png",outputStream);
        } catch (IOException e) {
            logger.error("响应认证失败",e.getMessage());
        }
    }

    @PostMapping("/login")
    public String userLogin(Model model,HttpServletRequest request,HttpServletResponse response,String username,String password,String code,boolean remember){
        String kaptcha=request.getSession().getAttribute("kaptcha").toString();
        if(StringUtils.isBlank(kaptcha)|| StringUtils.isBlank(code) ||!kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码错误");
            return "/site/login";
        }
        //如果记住我，时间长
        int expiredSeconds=remember==true?CommunityConstant.REMEMBER_EXPIRED_SECONDES:CommunityConstant.DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> login = userService.login(username, password, expiredSeconds);
        if(login.containsKey("ticket")){
            Cookie cookie=new Cookie("ticket",login.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }else{
            model.addAttribute("usernameMsg",login.get("usernameMsg"));
            model.addAttribute("passwordMsg",login.get("passwordMsg"));
            return  "/site/login";
        }
    }
    @GetMapping("/logout")
    public String logout(@CookieValue("ticket")String ticket){
        userService.logout(ticket);
        return "redirect:/login";
    }

}
