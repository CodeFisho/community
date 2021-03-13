package vip.xianyu.community.service;

import vip.xianyu.community.common.RespBean;
import vip.xianyu.community.entity.LoginTicket;
import vip.xianyu.community.entity.User;

import java.util.Map;

/**
 * @Author:asus
 * @Date: 2021/2/24 21:49
 * @Description:
 */
public interface IUserService {

    public User findUserById(int id);
    //注册功能
    public RespBean register(User user);

    //激活功能
    public int activation(int userId,String code);

    //登录
    public Map<String,Object> login(String username, String password,int expiredSeconds);

    //登出操作
    public void logout(String ticket);

    //通过ticket查询登录凭证
    public LoginTicket findLoginTicketByTicket(String ticket);

    //修改头像路径
    public int uploadHeader(int userId,String headerUrl);

}
