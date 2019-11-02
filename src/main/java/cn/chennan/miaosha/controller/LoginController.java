package cn.chennan.miaosha.controller;

import cn.chennan.miaosha.redis.RedisService;
import cn.chennan.miaosha.result.Result;
import cn.chennan.miaosha.service.MiaoshaUserService;
import cn.chennan.miaosha.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("login")
public class LoginController {

    @Autowired
    private MiaoshaUserService miaoshaUserService;
    @Autowired
    private RedisService redisService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVO){
        log.info(loginVO.toString());

        //参数校验
        /*String passwordInput = loginVO.getPassword();
        String mobileInput = loginVO.getMobile();
        if(StringUtils.isEmpty(passwordInput)){
            return Result.error(CodeMsg.PASSWRD_EMPTY);
        }
        if (StringUtils.isEmpty(mobileInput)) {
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        if(!ValidatorUtil.isMobile(mobileInput)){
            return Result.error(CodeMsg.MOBILE_ERROR);
        }*/

       String token = miaoshaUserService.login(response, loginVO);

        return Result.success(token);
    }
}
