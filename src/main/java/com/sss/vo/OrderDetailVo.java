package com.sss.vo;

import com.sss.domain.OrderInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author v_shishusheng
 * @date 2018/2/5
 */
@Setter
@Getter
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;
}