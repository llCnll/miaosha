package cn.chennan.miaosha.redis;

/**
 * @author ChenNan
 * @date 2019/10/24
 **/
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;
    private String prefix;

    @Override
    public int expireSeconds() {//默认0代表永不过期
        return expireSeconds;
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }
    public BasePrefix(String prefix) {
        this(0, prefix);
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
