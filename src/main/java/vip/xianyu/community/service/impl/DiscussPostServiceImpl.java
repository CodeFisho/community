package vip.xianyu.community.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import vip.xianyu.community.dao.DiscussPostMapper;
import vip.xianyu.community.entity.DiscussPost;
import vip.xianyu.community.service.IDiscussService;
import vip.xianyu.community.util.SensitiveFilter;

import java.util.List;

/**
 * @Author:asus
 * @Date: 2021/2/24 21:36
 * @Description:帖子服务实现类
 */
@Service
public class DiscussPostServiceImpl implements IDiscussService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter filter;

    @Override
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }

    @Override
    public int getDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    @Override
    public int addDiscussPost(DiscussPost discussPost) {
        if(discussPost==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //转义html标记
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        //过滤敏感词
        discussPost.setTitle(filter.filter(discussPost.getTitle()));
        discussPost.setContent(filter.filter(discussPost.getContent()));
        return discussPostMapper.insertDisscussPost(discussPost);

    }

    @Override
    public DiscussPost selectDisscussPostById(int id) {
        return discussPostMapper.selectDisscussPostById(id);
    }
}
