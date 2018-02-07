package com.sss.rabbitmq;

import com.sss.domain.SecKillOrder;
import com.sss.domain.User;
import com.sss.redis.RedisService;
import com.sss.service.GoodsService;
import com.sss.service.OrderService;
import com.sss.service.SecKillService;
import com.sss.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author v_shishusheng
 * @date 2018/2/6
 */
@Service
@Slf4j
public class MqReceiver {

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SecKillService SecKillService;

    @RabbitListener(queues = MqConfig.SECKILL_QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);
        SecKillMessage secKillMessage = RedisService.stringToBean(message, SecKillMessage.class);
        User user = secKillMessage.getUser();
        long goodsId = secKillMessage.getGoodsId();

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        //是否已秒到
        SecKillOrder order = orderService.getSecKillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        SecKillService.secKill(user, goods);
    }

}
