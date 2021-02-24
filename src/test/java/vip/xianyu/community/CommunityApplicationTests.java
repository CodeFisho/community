package vip.xianyu.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vip.xianyu.community.dao.DiscussPostMapper;
import vip.xianyu.community.dao.UserMapper;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    DiscussPostMapper discussMapper;
    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {
//        List<DiscussPost> discussPosts = discussMapper.selectDiscussPosts(0, 0, 1);
//        System.out.println(discussPosts);
        System.out.println(discussMapper.selectDiscussPosts(0,0,10));
        System.out.println(discussMapper.selectDiscussPostRows(0));
        System.out.println(userMapper.selectById(1));
    }

}
