package vip.xianyu.community.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.xianyu.community.entity.DiscussPost;

import java.util.List;

/**
 * @Author:asus
 * @Date: 2021/2/23 20:53
 * @Description:
 */

public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, @Param("offset") int offset,@Param("limit") int limit);
//    @Param注解用于给参数起别名；
//    如果只有一个参数，并且在<if>中，则必须要起别名
    int selectDiscussPostRows(@Param("userId") int userId);
}
