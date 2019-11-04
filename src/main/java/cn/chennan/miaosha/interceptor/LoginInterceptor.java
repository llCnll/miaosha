package cn.chennan.miaosha.interceptor;

import cn.chennan.miaosha.context.UserContext;
import cn.chennan.miaosha.domain.MiaoshaUser;
import cn.chennan.miaosha.result.CodeMsg;
import cn.chennan.miaosha.service.MiaoshaUserService;
import cn.chennan.miaosha.util.RenderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ChenNan
 * @date 2019-11-04 下午2:05
 **/
@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       if(handler instanceof HandlerMethod){
           HandlerMethod hm = (HandlerMethod)handler;
           Login login = hm.getMethodAnnotation(Login.class);
           if(login == null){
               return true;
           }
           log.info("login preHandle");

           String paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
           String cookieToken = getCookieValue(request, MiaoshaUserService.COOKIE_NAME_TOKEN);
           if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
               log.info(CodeMsg.NO_COOKIE.getMsg());
               RenderUtil.render(response, CodeMsg.NO_COOKIE);
               return false;
           }

           String token = StringUtils.isEmpty(cookieToken) ? paramToken : cookieToken;

           MiaoshaUser user = miaoshaUserService.getByToken(response, token);
           if(user == null){
               log.info(CodeMsg.SESSION_ERROR.getMsg());
               RenderUtil.render(response, CodeMsg.SESSION_ERROR);
               return false;
           }
           UserContext.setUser(user);

       }
       return true;
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(cookieName.equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("login postHandle");
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("login afterCompletion");
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("login afterConcurrentHandlingStarted");
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
