package vip.xianyu.community.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.xianyu.community.interceptor.CommunityInterceptor;
import vip.xianyu.community.interceptor.LoginRequiredInterceptor;

/**
 * @Author:asus
 * @Date: 2021/3/8 21:46
 * @Description:
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private CommunityInterceptor communityInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(communityInterceptor)
                .excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpg","/*/*.jpeg");
        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpg","/*/*.jpeg");
    }
}
