package cn.chennan.miaosha.redis;

/**
 * @author ChenNan
 * @date 2019/10/24
 **/
public class MiaoshaUserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 3600;

    private MiaoshaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE, "tk");
}
