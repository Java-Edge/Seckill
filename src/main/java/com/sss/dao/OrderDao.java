package com.sss.dao;

import com.sss.domain.OrderInfo;
import com.sss.domain.SecKillOrder;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author v_shishusheng
 * @date 2018/2/2
 */
@Mapper
@Repository
public interface OrderDao {

    /**
     *
     * @param userId
     * @param goodsId
     * @return
     */
    @Select("select * from seckill_order where user_id=#{userId} and goods_id=#{goodsId}")
    SecKillOrder getSecKillOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    long insert(OrderInfo orderInfo);

    @Insert("insert into seckill_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    int insertSecKillOrder(SecKillOrder seckillOrder);

    @Select("select * from order_info where id = #{orderId}")
    OrderInfo getOrderById(@Param("orderId")long orderId);
}
