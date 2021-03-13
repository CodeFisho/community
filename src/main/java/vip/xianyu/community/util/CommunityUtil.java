package vip.xianyu.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author:asus
 * @Date: 2021/3/2 23:04
 * @Description:
 */
@Component
public class CommunityUtil {

    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
    //md5加密
    public static String md5(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
    //将数据转换为json格式
    public static String getJSONString(int code, String msg, Map<String,Object> map){
        JSONObject json=new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map!=null){
            for(String key:map.keySet()){
                json.put(key,map.get(key));
            }
        }
        return json.toJSONString();
    }
    public static String getJSONString(int code,String msg){
        return getJSONString(code,msg,null);
    }
    public static String  getJSONString(int code){
        return getJSONString(code,null,null);
    }

    public static void main(String[] args) {
        Map<String,Object> map=new HashMap<>();
        map.put("name","张三");
        map.put("age",13);
        System.out.println(getJSONString(200,"成功",map));
    }
}
