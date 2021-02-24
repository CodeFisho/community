package vip.xianyu.community.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.xianyu.community.dao.DiscussPostMapper;
import vip.xianyu.community.entity.DiscussPost;
import vip.xianyu.community.service.IDiscussService;

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

    @Override
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }

    @Override
    public int getDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
