package cn.chennan.miaosha.config;

import cn.chennan.miaosha.domain.MiaoshaUser;
import cn.chennan.miaosha.exception.GlobalException;
import cn.chennan.miaosha.result.CodeMsg;
import cn.chennan.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ChenNan
 * @date 2019-10-24 下午8:46
 **/
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private MiaoshaUserService miaoshaUserService;
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == MiaoshaUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, MiaoshaUserService.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            throw new GlobalException(CodeMsg.NO_COOKIE);
        }

        String token = StringUtils.isEmpty(cookieToken) ? paramToken : cookieToken;

        MiaoshaUser user = miaoshaUserService.getByToken(response, token);
        if(user == null){
            throw new GlobalException(CodeMsg.SESSION_ERROR);
        }
        return user;
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        String cookieToken;
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
}
