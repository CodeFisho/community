package vip.xianyu.community.service;

import vip.xianyu.community.entity.User;

/**
 * @Author:asus
 * @Date: 2021/2/24 21:49
 * @Description:
 */
public interface IUserService {
    public User findUserById(int id);
}
