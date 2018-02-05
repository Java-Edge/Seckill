package com.sss.service;

import com.sss.domain.OrderInfo;
import com.sss.domain.User;
import com.sss.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author v_shishusheng
 * @date 2018/2/2
 */
@Service
public class SecKillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo secKill(User user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        goodsService.reduceStock(goods);
        //order_info seckill_order
        return orderService.createOrder(user, goods);
    }

}
