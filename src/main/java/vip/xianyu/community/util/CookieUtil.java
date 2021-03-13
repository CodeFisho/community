package vip.xianyu.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author:asus
 * @Date: 2021/3/8 22:39
 * @Description:
 */
public class CookieUtil {
    public static String getValue(HttpServletRequest request,String keyName){
        if(request ==null || keyName==null){
            throw new IllegalArgumentException("参数错误");
        }
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(keyName)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
