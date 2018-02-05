package com.sss.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author v_shishusheng
 * @date 2018/2/1
 */
@Setter
@Getter
public class SecKillOrder {
    private Long id;
    private Long userId;
    private Long  orderId;
    private Long goodsId;
}
