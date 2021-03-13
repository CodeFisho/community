package vip.xianyu.community.util;

import org.springframework.stereotype.Component;
import vip.xianyu.community.entity.User;

/**
 * @Author:asus
 * @Date: 2021/3/8 22:57
 * @Description:持有用户信息，用于持有session对象
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users=new ThreadLocal<User>();

    public void setUser(User user){
        users.set(user);
    }
    public User getUser(){
        return users.get();
    }
    public void clear(){
        users.remove();
    }
}
