package com.sss.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author v_shishusheng
 * @date 2018/2/1
 */
@Setter
@Getter
public class Goods {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImage;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
}
