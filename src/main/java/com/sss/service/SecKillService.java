package com.sss.service;

import com.sss.domain.OrderInfo;
import com.sss.domain.SecKillOrder;
import com.sss.domain.User;
import com.sss.redis.RedisService;
import com.sss.redis.SecKillKey;
import com.sss.util.MD5Util;
import com.sss.util.UUIDUtil;
import com.sss.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo secKill(User user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        if(success) {
            //order_info seckill_order
            return orderService.createOrder(user, goods);
        }else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    public long getSecKillResult(Long userId, long goodsId) {
        SecKillOrder order = orderService.getSecKillOrderByUserIdGoodsId(userId, goodsId);
        //秒杀成功
        if(order != null) {
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            }else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SecKillKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SecKillKey.isGoodsOver, ""+goodsId);
    }

    public void reset(List<GoodsVo> goodsList) {
        goodsService.resetStock(goodsList);
        orderService.deleteOrders();
    }

    public boolean checkPath(User user, long goodsId, String path) {
        if(user == null || path == null) {
            return false;
        }
        String pathOld = redisService.get(SecKillKey.getSecKillPath, ""+user.getId() + "_"+ goodsId, String.class);
        return path.equals(pathOld);
    }

    public String createSecKillPath(User user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisService.set(SecKillKey.getSecKillPath, ""+user.getId() + "_"+ goodsId, str);
        return str;
    }
}
