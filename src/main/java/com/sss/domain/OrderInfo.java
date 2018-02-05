package com.sss.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author v_shishusheng
 * @date 2018/2/1
 */
@Setter
@Getter
public class OrderInfo {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Long  deliveryAddrId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;
    private Date createDate;
    private Date payDate;
}
