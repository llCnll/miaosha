package cn.chennan.miaosha.controller;

import cn.chennan.miaosha.domain.MiaoshaUser;
import cn.chennan.miaosha.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("user")
public class MiaoshaUserController {

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model, MiaoshaUser user){
        log.info(user.getId()+"cn.chennan.miaosha.controller.MiaoshaUserController.info");
        return Result.success(user);
    }
}
