package cn.chennan.miaosha.context;

import cn.chennan.miaosha.domain.MiaoshaUser;

/**
 * @author ChenNan
 * @date 2019-11-04 下午2:21
 **/
public class UserContext {
    public static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();

    public static void setUser(MiaoshaUser user){
        userHolder.set(user);
    }

    public static MiaoshaUser getUser(){
        return userHolder.get();
    }
}
