package vip.xianyu.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vip.xianyu.community.entity.DiscussPost;
import vip.xianyu.community.entity.User;
import vip.xianyu.community.service.IDiscussService;
import vip.xianyu.community.service.IUserService;
import vip.xianyu.community.service.impl.UserServiceImpl;
import vip.xianyu.community.util.CommunityUtil;
import vip.xianyu.community.util.HostHolder;

import java.util.Date;

/**
 * @Author:asus
 * @Date: 2021/3/11 20:55
 * @Description:
 */
@Controller
@RequestMapping("/discuss")
public class DisscussPostController {
    @Autowired
    private IDiscussService discussService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private IUserService userService;

    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user = hostHolder.getUser();
        if(user == null){
            return CommunityUtil.getJSONString(403,"未登录!");
        }
        if(StringUtils.isBlank(title)){
            return CommunityUtil.getJSONString(500,"请填写标题");
        }
        if(StringUtils.isBlank(content)){
            return CommunityUtil.getJSONString(500,"请填写帖子内容");
        }
        DiscussPost discussPost=new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        int result = discussService.addDiscussPost(discussPost);

        //报错的情况，统一处理
        return CommunityUtil.getJSONString(0,"发布成功!");
    }

    @GetMapping("/detail/{disscussPostId}")
    public String getDetail(@PathVariable("disscussPostId") int disscussPostId, Model model){
        DiscussPost discussPost = discussService.selectDisscussPostById(disscussPostId);
        model.addAttribute("post",discussPost);
        User auth = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user",auth);
        //todo:补充帖子的回复
        return "/site/discuss-detail";
    }
}
