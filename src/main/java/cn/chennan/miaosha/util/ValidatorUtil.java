package cn.chennan.miaosha.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ChenNan
 * @date 2019/10/24
 **/
public class ValidatorUtil {

    private static final Pattern mobild_pattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String mobile){
        if(StringUtils.isEmpty(mobile)){
            return false;
        }else{
            Matcher m = mobild_pattern.matcher(mobile);
            return m.matches();
        }
    }
}
