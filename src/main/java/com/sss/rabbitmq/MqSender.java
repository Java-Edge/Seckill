package com.sss.rabbitmq;

import com.sss.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author v_shishusheng
 * @date 2018/2/6
 */
@Service
@Slf4j
public class MqSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendSecKillMessage(SecKillMessage mm) {
        String msg = RedisService.beanToString(mm);
        log.info("send message:"+msg);
        amqpTemplate.convertAndSend(MqConfig.SECKILL_QUEUE, msg);
    }
}
