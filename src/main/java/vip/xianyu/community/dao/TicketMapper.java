package vip.xianyu.community.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import vip.xianyu.community.entity.LoginTicket;

import javax.annotation.Generated;

/**
 * @Author:asus
 * @Date: 2021/3/6 15:45
 * @Description:
 */
public interface TicketMapper {
    @Insert({"insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTickey(String ticket);
    @Update({
            "update login_ticket set status=#{status} where ticket=#{ticket}"
    })
    int updateLoginTicketStatus(String ticket,int status);
}
