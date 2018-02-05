package com.sss.dao;

import com.sss.domain.Goods;
import com.sss.domain.SecKillGoods;
import com.sss.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author v_shishusheng
 * @date 2018/2/1
 */
@Mapper
@Repository
public interface GoodsDao {

    //todo 左连接!!!

    /**
     *
     * @return
     */
    @Select("select goods.*,seckill_goods.stock_count, seckill_goods.start_date, seckill_goods.end_date,seckill_goods.seckill_price from seckill_goods seckill_goods left join goods goods on seckill_goods.goods_id = goods.id")
    List<GoodsVo> listGoodsVo();

    /**
     *
     * @param goodsId
     * @return
     */
    @Select("select goods.*,seckill_goods.stock_count, seckill_goods.start_date, seckill_goods.end_date,seckill_goods.seckill_price from seckill_goods seckill_goods left join goods goods on seckill_goods.goods_id = goods.id where goods.id = #{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long goodsId);

    /**
     *
     * @param goods
     * @return
     */
    @Update("update seckill_goods set stock_count = stock_count - 1 where goods_id = #{goodsId}")
    int reduceStock(SecKillGoods goods);
}
