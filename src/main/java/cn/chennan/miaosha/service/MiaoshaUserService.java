package cn.chennan.miaosha.service;

import cn.chennan.miaosha.dao.MiaoshaUserDao;
import cn.chennan.miaosha.domain.MiaoshaUser;
import cn.chennan.miaosha.exception.GlobalException;
import cn.chennan.miaosha.redis.MiaoshaUserKey;
import cn.chennan.miaosha.redis.RedisService;
import cn.chennan.miaosha.result.CodeMsg;
import cn.chennan.miaosha.util.MD5Util;
import cn.chennan.miaosha.util.UUIDUtil;
import cn.chennan.miaosha.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ChenNan
 * @date 2019/10/24
 **/
@Service
@Slf4j
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private MiaoshaUserDao miaoshaUserDao;

    @Autowired
    private RedisService redisService;

    public MiaoshaUser getById(Long id){
        return miaoshaUserDao.getById(id);
    }

    public String login(HttpServletResponse response, LoginVo loginVO) {

        if(loginVO == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String fromPass = loginVO.getPassword();
        String mobile = loginVO.getMobile();

        MiaoshaUser user = getById(Long.parseLong(mobile));

        if(user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        log.info(user.toString());
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.FormPassToDBPass(fromPass, saltDB);
        if(!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWRD_ERROR);
        }

        String token = addCookie(response, user, null);

        return token;
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if(user != null){
            addCookie(response, user, token);
        }

        return user;
    }

    private String addCookie(HttpServletResponse response, MiaoshaUser user, String token){
        //生成token
        if(token == null) {
            token = UUIDUtil.uuid();
        }

        redisService.set( MiaoshaUserKey.token, token, user);

        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.TOKEN_EXPIRE);
        cookie.setPath("/");
        response.addCookie(cookie);
        return token;
    }
}
