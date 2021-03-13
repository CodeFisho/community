package vip.xianyu.community.interceptor;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import vip.xianyu.community.entity.LoginTicket;
import vip.xianyu.community.entity.User;
import vip.xianyu.community.service.IUserService;
import vip.xianyu.community.util.CookieUtil;
import vip.xianyu.community.util.HostHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author:asus
 * @Date: 2021/3/8 21:22
 * @Description:
 */
@Component
public class CommunityInterceptor implements HandlerInterceptor {
    private static  final Logger logger= LoggerFactory.getLogger(CommunityInterceptor.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private HostHolder hostHolder;
    //在controller之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket= CookieUtil.getValue(request,"ticket");
        if(ticket!=null){
            LoginTicket loginTicketByTicket = userService.findLoginTicketByTicket(ticket);
            //检查凭证是否有效
            if(ticket!=null && loginTicketByTicket.getStatus()==0 && loginTicketByTicket.getExpired().after(new Date())){
                User user=userService.findUserById(loginTicketByTicket.getUserId());
                hostHolder.setUser(user);
            }
        }
        logger.debug("pre"+handler.toString());
        return true;
    }
    //在controller后执行

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user!=null && modelAndView!=null){
            modelAndView.addObject("loginUser",user);
        }

        logger.debug("postHandle"+handler.toString());
    }

    //在模板引擎执行之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        User user=hostHolder.getUser();
        if(null!=user){
            hostHolder.clear();
        }
        logger.debug("afterHandle"+handler.toString());
    }
}
