package cn.chennan.miaosha.dao;

import cn.chennan.miaosha.domain.MiaoshaOrder;
import cn.chennan.miaosha.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

/**
 * @author ChenNan
 * @date 2019-10-25 下午5:06
 **/
@Mapper
public interface OrderDao {
    @Select("select * from miaosha_order where user_id = #{userId} and goods_id = #{goodsId}")
    MiaoshaOrder getMiaoShaOrderByUserIdGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId);

    @Insert("insert into order_info(user_id, goods_id, delivery_addr_id, goods_name, goods_count, goods_price, order_channel, status, create_date) " +
            "values(#{userId}, #{goodsId}, #{deliveryAddrId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate})")
    @SelectKey( keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    long insert(OrderInfo orderInfo);

    @Insert("insert into miaosha_order(user_id, order_id, goods_id) values(#{userId}, #{orderId}, #{goodsId})")
    void insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    @Select("select * from order_info where id = #{orderId}")
    OrderInfo getOrderById(@Param("orderId") long orderId);
}
