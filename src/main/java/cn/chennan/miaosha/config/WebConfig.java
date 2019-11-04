package cn.chennan.miaosha.config;

import cn.chennan.miaosha.interceptor.AccessInterceptor;
import cn.chennan.miaosha.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author ChenNan
 * @date 2019-10-24 下午8:44
 **/
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private UserArgumentResolver userArgumentResolver;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private AccessInterceptor accessInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userArgumentResolver);
    }

    /**
     * 根据添加的先后既是执行顺序 先执行 loginInterceptor  在执行 accessInterceptor
     *
     login preHandle
     access preHandle
     service()方法
     AccessLimit postHandle
     login postHandle
     AccessLimit afterCompletion
     login afterCompletion
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor);
        registry.addInterceptor(accessInterceptor);
    }

}

