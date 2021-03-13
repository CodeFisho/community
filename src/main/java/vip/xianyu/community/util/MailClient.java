package vip.xianyu.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Author:asus
 * @Date: 2021/3/2 21:06
 * @Description:
 */
@Component
public class MailClient {
    private static final Logger logger= LoggerFactory.getLogger(MailClient.class);
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    public void sendEmail(String to,String subject,String content){
        try {
            MimeMessage message=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("邮件发送失败"+e);
        }

    }
    //发送模板邮件
    public void sentPublishMailTemplateDemo(){
        //1.创建Content 对象，由该对象对模板中的变量（例如：<h1 th:text=${username}>...</h1>）赋值
        Context context=new Context();
        context.setVariable("username","hello ");
        //为变量赋值后，将内容交由模板引擎进行处理，处理后的返回值为字符串
        //第一个参数为被发送邮件的路径
        String content = templateEngine.process("/mail/activation", context);

        //经过模板引擎的处理后，再将其发送
        sendEmail("1085108360@qq.com","测试",content);

    }
}
