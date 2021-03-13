package vip.xianyu.community.service;

import vip.xianyu.community.entity.DiscussPost;

import java.util.List;

/**
 * @Author:asus
 * @Date: 2021/2/24 21:33
 * @Description:
 */
public interface IDiscussService {

    public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit);

    public int getDiscussPostRows(int userId);

    public int addDiscussPost(DiscussPost discussPost);

    public DiscussPost selectDisscussPostById(int id);
}
