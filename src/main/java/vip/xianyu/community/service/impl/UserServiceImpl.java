package vip.xianyu.community.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import vip.xianyu.community.common.RespBean;
import vip.xianyu.community.dao.TicketMapper;
import vip.xianyu.community.dao.UserMapper;
import vip.xianyu.community.entity.LoginTicket;
import vip.xianyu.community.entity.User;
import vip.xianyu.community.service.IUserService;
import vip.xianyu.community.util.CommunityConstant;
import vip.xianyu.community.util.CommunityUtil;
import vip.xianyu.community.util.MailClient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author:asus
 * @Date: 2021/2/24 21:49
 * @Description:
 */
@Service
public class UserServiceImpl implements IUserService, CommunityConstant {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private TicketMapper ticketMapper;
    //从配置文件读取域名和项目名
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Override
    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    @Override
    public RespBean register(User user) {
        RespBean respBean=RespBean.error(null);
        if(null == user){
            throw  new IllegalArgumentException("参数不能为空");
        }
        if(StringUtils.isBlank(user.getUsername())){
            respBean.setMsg("账号不能为空");
            return respBean;
        }
        if(StringUtils.isBlank(user.getPassword())){
            respBean.setMsg("密码不能为空");
            return respBean;
        }
        if(StringUtils.isBlank(user.getEmail())){
            respBean.setMsg("邮箱不能为空");
            return respBean;
        }
        //校验账号是否已经存在
        User u=userMapper.selectByName(user.getUsername());
        if(u!=null){
            respBean.setMsg("该账号已经存在");
            return respBean;
        }
        u=userMapper.selectByEmail(user.getEmail());
        if(u!=null){
            respBean.setMsg("邮箱已经注册");
            return respBean;
        }
        //生成盐值
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        //对密码加密
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("images.nowcode.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        int i = userMapper.insertUser(user);
        if(i<=0){
            respBean.setMsg("注册失败，请联系管理员");
            return respBean;
        }
        //为用户发送一封邮件，用于激活账号
        Context context=new Context();
        String url=domain+contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("email",user.getEmail());
        context.setVariable("url",url);

        String html=templateEngine.process("/mail/activation",context);
        respBean.setCode(200);
        respBean.setMsg("注册成功,我们将向您的邮箱发送一封邮件，请注意查收");
        mailClient.sendEmail(user.getEmail(),"激活您的账号",html);
        return respBean;
    }

    @Override
    public int activation(int userId, String code) {
        User user=userMapper.selectById(userId);
        if(null == user){
            return ACTIVATION_FAILURE;
        }
        if(user.getStatus()==1){
            return ACTIVATION_REPEAT;
        }
        if(!code.equals(user.getActivationCode())){
            return ACTIVATION_FAILURE;
        }
        int i = userMapper.updateStatus(userId, 1);
        if(i<=0){
            return ACTIVATION_FAILURE;
        }
        return ACTIVATION_SUCCESS;
    }

    @Override
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String,Object> result=new HashMap<>();
        if(StringUtils.isBlank(username)){
            result.put("usernameMsg","账号不能为空");
            return result;
        }
        if(StringUtils.isBlank(password)){
            result.put("passwordMsg","密码不能为空");
            return result;
        }
        //验证账号
        User user = userMapper.selectByName(username);
        if(null == user){
            result.put("usernameMsg","该账号不存在");
            return result;
        }
        if(user.getStatus()==0){
            result.put("usernameMsg","该账号未激活");
            return result;
        }
        //验证密码
        if(!CommunityUtil.md5(password+user.getSalt()).equals(user.getPassword())){
            result.put("passwordMsg","密码错误");
            return result;
        }
        //验证通过
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(user.getId());
        ticket.setTicket(CommunityUtil.generateUUID());
        ticket.setStatus(0);
        ticket.setExpired(new Date(System.currentTimeMillis()+expiredSeconds));
        ticketMapper.insertLoginTicket(ticket);
        result.put("ticket",ticket.getTicket());
        return result;
    }

    @Override
    public void logout(String ticket) {
        ticketMapper.updateLoginTicketStatus(ticket,1);
    }

    @Override
    public LoginTicket findLoginTicketByTicket(String ticket) {
        return ticketMapper.selectByTickey(ticket);
    }

    @Override
    public int uploadHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId,headerUrl);
    }
}
