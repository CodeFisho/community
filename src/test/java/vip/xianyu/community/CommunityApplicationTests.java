package vip.xianyu.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import vip.xianyu.community.dao.DiscussPostMapper;
import vip.xianyu.community.dao.TicketMapper;
import vip.xianyu.community.dao.UserMapper;
import vip.xianyu.community.entity.DiscussPost;
import vip.xianyu.community.entity.LoginTicket;
import vip.xianyu.community.entity.User;
import vip.xianyu.community.service.IDiscussService;
import vip.xianyu.community.service.IUserService;
import vip.xianyu.community.util.MailClient;
import vip.xianyu.community.util.SensitiveFilter;

import java.util.*;

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
    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private MailClient mailClient;
    @Autowired
    private SensitiveFilter filter;

    @Autowired
    private TemplateEngine templateEngine;
    @Test
    void contextLoads() {
        List<DiscussPost> discussPosts = discussMapper.selectDiscussPosts(0, 0, 1);
        System.out.println(discussPosts);


    }
    @Test
    void mailTest(){
        mailClient.sendEmail("1085108360@qq.com","测试邮件发送","hello world");
    }
    @Test
    void mailTemplate(){
        //为html文件中的变量赋值
        Context context=new Context();
        context.setVariable("username","xiaoming");
        //
        String content = templateEngine.process("/mail/activation", context);
        System.out.println(content);

        mailClient.sendEmail("1085108360@qq.com","测试邮件发送",content);

    }
    @Test
    public void sqlAno(){
//        LoginTicket l=new LoginTicket();
//        l.setStatus(0);
//        l.setExpired(new Date(System.currentTimeMillis()+1000*60));
//        l.setTicket("abc");
//        l.setUserId(101);
//        int i = ticketMapper.insertLoginTicket(l);
//        System.out.println(i);
        LoginTicket abc = ticketMapper.selectByTickey("abc");
        System.out.println(abc);
        ticketMapper.updateLoginTicketStatus("abc",1);
        abc = ticketMapper.selectByTickey("abc");
        System.out.println(abc);
    }
    @Test
    public void test00(){
        String result = filter.filter("这里可以吸毒");
        System.out.println(result);
    }

}
