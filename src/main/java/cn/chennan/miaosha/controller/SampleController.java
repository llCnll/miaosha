package cn.chennan.miaosha.controller;

import cn.chennan.miaosha.domain.MiaoshaUser;
import cn.chennan.miaosha.domain.User;
import cn.chennan.miaosha.exception.GlobalException;
import cn.chennan.miaosha.rabbitmq.MQSender;
import cn.chennan.miaosha.redis.RedisService;
import cn.chennan.miaosha.redis.UserKey;
import cn.chennan.miaosha.result.CodeMsg;
import cn.chennan.miaosha.result.Result;
import cn.chennan.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("demo")
public class SampleController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender sender;

    @RequestMapping("/mqHeader")
    @ResponseBody
    public Result<String> myHeader(Model model) {
        sender.sendHeader("hello");
        return Result.success("hello");
    }
    @RequestMapping("/mqFanout")
    @ResponseBody
    public Result<String> myFanout(Model model) {
        sender.sendFanout("hello");
        return Result.success("hello");
    }
    @RequestMapping("/mqTopic")
    @ResponseBody
    public Result<String> mqTopic(Model model) {
        sender.sendTopic("hello");
        return Result.success("hello");
    }
    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq(Model model){
        sender.send("hello");
        return Result.success("hello");
    }
    @RequestMapping("/demo")
    public String thymeleaf(Model model){
        model.addAttribute("name", "cn");
        return "hello";
    }
    @RequestMapping("user")
    @ResponseBody
    public Result<MiaoshaUser> user(MiaoshaUser user){
        return Result.success(user);
    }
    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello(){
        return Result.success("hello spring boot");
    }
    @RequestMapping("/error")
    //@ResponseBody
    public Result<String> error(){
        throw new GlobalException(CodeMsg.NO_COOKIE);
        //throw new GlobalException(CodeMsg.SERVER_ERROR);
        //return Result.error(CodeMsg.SERVER_ERROR);
    }
    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet(int id){
        User user = userService.getById(id);
        return Result.success(user);
    }
    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx(){
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet(){
        User user = redisService.get(UserKey.getById, ""+1, User.class);
        return Result.success(user);
    }
    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user = new User();
        user.setName("cn");
        user.setId(1);
        boolean flag = redisService.set(UserKey.getById, ""+user.getId(), user);
        return Result.success(flag);
    }

}
