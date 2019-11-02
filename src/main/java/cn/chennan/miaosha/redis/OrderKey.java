package cn.chennan.miaosha.redis;

/**
 * @author ChenNan
 * @date 2019/10/24
 **/
public class OrderKey extends BasePrefix {
    private OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
