package vip.xianyu.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vip.xianyu.community.dao.DiscussPostMapper;
import vip.xianyu.community.dao.UserMapper;
import vip.xianyu.community.entity.DiscussPost;
import vip.xianyu.community.entity.User;
import vip.xianyu.community.service.IDiscussService;
import vip.xianyu.community.service.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    DiscussPostMapper discussMapper;
    @Autowired
    UserMapper userMapper;

    @Autowired
    IDiscussService discussService;
    @Autowired
    IUserService userService;
    @Test
    void contextLoads() {
        List<DiscussPost> discussPosts = discussMapper.selectDiscussPosts(0, 0, 1);
        System.out.println(discussPosts);


    }

}
