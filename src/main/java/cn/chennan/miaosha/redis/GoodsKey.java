package cn.chennan.miaosha.redis;

/**
 * @author ChenNan
 * @date 2019/10/24
 **/
public class GoodsKey extends BasePrefix {

    private GoodsKey(String prefix, int expiredSeconds) {
        super(expiredSeconds, prefix);
    }
    public static GoodsKey getGoodsList = new GoodsKey("gl", 60);
    public static GoodsKey getGoodsDetail = new GoodsKey("gd", 60);
    public static GoodsKey getMiaoshaGoodsStock = new GoodsKey("gs",0);
}
