package cn.chennan.miaosha.redis;

/**
 * @author ChenNan
 * @date 2019/10/24
 **/
public class UserKey extends BasePrefix {

    private UserKey(String prefix) {
        super(prefix);
    }
    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
