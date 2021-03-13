package vip.xianyu.community.util;

/**
 * @Author:asus
 * @Date: 2021/3/4 20:33
 * @Description:激活类型常量接口
 */
public interface CommunityConstant {
    /*
    激活成功
    * */
    public static final int ACTIVATION_SUCCESS=0;
    /**
     *重复激活
     */
    public static final int ACTIVATION_REPEAT=1;
    /**
     * 激活失败
     */
    public static final int ACTIVATION_FAILURE=2;

    //默认登录状态登录凭证的超时时间
    public static final int DEFAULT_EXPIRED_SECONDS=3600*12;

    //点击记住我后
    public static final int REMEMBER_EXPIRED_SECONDES=3600*24*100;
}
