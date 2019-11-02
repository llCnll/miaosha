package cn.chennan.miaosha.util;

import java.util.UUID;

/**
 * @author ChenNan
 * @date 2019/10/24
 **/
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
