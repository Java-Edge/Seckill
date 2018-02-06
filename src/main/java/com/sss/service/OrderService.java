package com.sss.service;

import java.util.Date;

import com.sss.redis.OrderKey;
import com.sss.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sss.dao.OrderDao;
import com.sss.domain.SecKillOrder;
import com.sss.domain.User;
import com.sss.domain.OrderInfo;
import com.sss.vo.GoodsVo;

/**
 * @author v_shishusheng
 */
@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;

    public SecKillOrder getSecKillOrderByUserIdGoodsId(long userId, long goodsId) {
        return redisService.get(OrderKey.getSecKillOrderByUidGid, ""+userId+"_"+goodsId, SecKillOrder.class);
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }
    @Transactional
    public OrderInfo createOrder(User user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSecKillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        long orderId = orderDao.insert(orderInfo);
        SecKillOrder secKillOrder = new SecKillOrder();
        secKillOrder.setGoodsId(goods.getId());
        secKillOrder.setOrderId(orderId);
        secKillOrder.setUserId(user.getId());
        orderDao.insertSecKillOrder(secKillOrder);

        redisService.set(OrderKey.getSecKillOrderByUidGid, ""+user.getId()+"_"+goods.getId(), secKillOrder);
        return orderInfo;
    }

}
