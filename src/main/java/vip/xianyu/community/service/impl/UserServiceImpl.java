package vip.xianyu.community.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.xianyu.community.entity.User;
import vip.xianyu.community.service.IUserService;

/**
 * @Author:asus
 * @Date: 2021/2/24 21:49
 * @Description:
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserService userService;
    @Override
    public User findUserById(int userId) {
        return userService.findUserById(userId);
    }
}
