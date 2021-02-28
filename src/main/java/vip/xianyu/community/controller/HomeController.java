package vip.xianyu.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vip.xianyu.community.common.Page;
import vip.xianyu.community.entity.DiscussPost;
import vip.xianyu.community.entity.User;
import vip.xianyu.community.service.IDiscussService;
import vip.xianyu.community.service.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:asus
 * @Date: 2021/2/24 22:23
 * @Description:
 */
@Controller
public class HomeController {

    @Autowired
    private IDiscussService discussService;
    @Autowired
    private IUserService userService;

    @GetMapping("/index")
    public String getIndexPage(Model model, Page page){
        //方法调用前，SpringMVC会自动实例化Model和Page，并且会将page注入到model中
        page.setRows(discussService.getDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list=discussService.findDiscussPosts(0,page.getOffset(),page.getLimit());
        List<Map<String,Object>> discuss=new ArrayList<>();
        if(null != list){
            for (DiscussPost discussPost : list) {
                Map<String,Object> map=new HashMap<>();
                map.put("post",discussPost);
                User user=userService.findUserById(discussPost.getUserId());
                map.put("user",user);
                discuss.add(map);
            }
        }
        model.addAttribute("discussPosts",discuss);
        return "index";
    }
}
