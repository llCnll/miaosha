package cn.chennan.miaosha.dao;

import cn.chennan.miaosha.domain.MiaoshaGoods;
import cn.chennan.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author ChenNan
 * @date 2019-10-25 下午3:16
 **/
@Mapper
public interface GoodsDao {

    @Select("select g.*, mg.miaosha_price, mg.stock_count, mg.start_date, mg.end_date from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select g.*, mg.miaosha_price, mg.stock_count, mg.start_date, mg.end_date from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") Long goodsId);

    @Update("update miaosha_goods set stock_count = stock_count -1 where goods_id = #{goodsId} and stock_count > 0")
    int reduceStock(MiaoshaGoods g);
}
