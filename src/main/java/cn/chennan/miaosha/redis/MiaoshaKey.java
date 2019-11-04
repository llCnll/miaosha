package cn.chennan.miaosha.redis;

/**
 * @author ChenNan
 * @date 2019/10/24
 **/
public class MiaoshaKey extends BasePrefix {

    private MiaoshaKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(60, "mp");
}
