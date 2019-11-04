package cn.chennan.miaosha.interceptor;

import cn.chennan.miaosha.context.UserContext;
import cn.chennan.miaosha.redis.AccessKey;
import cn.chennan.miaosha.redis.RedisService;
import cn.chennan.miaosha.result.CodeMsg;
import cn.chennan.miaosha.util.RenderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ChenNan
 * @date 2019-11-04 下午1:26
 **/
@Component
@Slf4j
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod)handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null){
                return true;
            }
            log.info("access preHandle");

            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            Long userId = UserContext.getUser().getId();
            String uri = request.getRequestURI();
            String key = uri + "_" + userId;

            AccessKey ak = AccessKey.withExpire(seconds);
            Integer count = redisService.get(ak, key, Integer.class);
            if(count == null){
                redisService.set(ak, key, 1);
            }else if(count <= 5){
                redisService.incr(ak, key);
            }else{
                log.info(userId+"频繁访问");

                RenderUtil.render(response, CodeMsg.ACCESS_LIMIT);
                return false;
            }

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("AccessLimit postHandle");
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("AccessLimit afterCompletion");
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("AccessLimit afterConcurrentHandlingStarted");
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
