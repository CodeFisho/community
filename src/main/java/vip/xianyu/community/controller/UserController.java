package vip.xianyu.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import vip.xianyu.community.annotation.LoginRequired;
import vip.xianyu.community.entity.User;
import vip.xianyu.community.service.IUserService;
import vip.xianyu.community.util.CommunityUtil;
import vip.xianyu.community.util.HostHolder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author:asus
 * @Date: 2021/3/9 20:11
 * @Description:
 */
@Controller
@RequestMapping("/user")
public class UserController {
    public static final Logger logger= LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private HostHolder hostHolder;

    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;


    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage(){
        return "/site/setting";
    }
    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImg, Model model){
        if(headerImg==null){
            model.addAttribute("error","请选择图片");
            return "/site/setting";
        }
        String imgName=headerImg.getOriginalFilename();
        String fileType=imgName.substring(imgName.lastIndexOf("."));
        if(StringUtils.isBlank(fileType)){
            model.addAttribute("error","图片格式错误");
            return "/site/setting";
        }
//        if(!"jpg,png".contains(fileType)){
//            model.addAttribute("error","请选择图片！");
//            return "/site/setting";
//        }
        imgName= CommunityUtil.generateUUID()+fileType;
        File dest=new File(uploadPath+"/"+imgName);
        if(!dest.exists()){
            dest.mkdirs();
        }
        try {
            //存储文件
            headerImg.transferTo(dest);
        } catch (IOException e) {
           logger.error("img upload error:"+e.getMessage());
           throw new RuntimeException("上传文件失败,服务器发生异常");
        }
        //上传成功，修改头像的路径()
        User user=hostHolder.getUser();
        String headerUrl=domain+contextPath+"/user/header/"+imgName;
        userService.uploadHeader(user.getId(),headerUrl);
        return "redirect:/index";
    }
    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName")String fileName, HttpServletResponse response){
        //服务器存放路径
        fileName=uploadPath+"/"+fileName;
        //文件后缀
        String fileType=fileName.substring(fileName.lastIndexOf("."));
        response.setContentType("image/"+fileType);
        try(FileInputStream fis=new FileInputStream(fileName);) {
            OutputStream os = response.getOutputStream();

            byte[] buffer=new byte[1024];
            int b=0;
            while((b=fis.read(buffer))!=-1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败:"+e.getMessage());
        }
    }

}
