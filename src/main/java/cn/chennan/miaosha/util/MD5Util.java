package cn.chennan.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author ChenNan
 * @date 2019/10/24
 **/
public class MD5Util {
    private static final String salt = "1a2b3c4d";
    public static String md5(String str){
        return DigestUtils.md5Hex(str);
    }
    public static String inputPassToFormPass(String inputPass){
        String str = ""+salt.charAt(0)+salt.charAt(1)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }
    public static String FormPassToDBPass(String formPass, String salt){
        String str = ""+salt.charAt(0)+salt.charAt(1)+formPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }
    public static String inputPassToDBPass(String inputPass, String saltDB){
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = FormPassToDBPass(formPass, saltDB);
        return  dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDBPass("123456", "1a2b3c4d"));
        System.out.println(inputPassToFormPass("123456"));
        System.out.println(FormPassToDBPass(inputPassToFormPass("123456"), "1a2b3c4d"));
    }
}
