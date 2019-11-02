package cn.chennan.miaosha.redis;

/**
 * @author ChenNan
 * @date 2019/10/24
 **/
public interface KeyPrefix {
    public int expireSeconds();

    public String getPrefix();
}
