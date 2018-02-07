package com.sss.rabbitmq;

import com.sss.domain.User;
import lombok.Getter;
import lombok.Setter;

/**
 * @author v_shishusheng
 * @date 2018/2/7
 */
@Setter
@Getter
public class SecKillMessage {
    private User user;
    private long goodsId;

}
