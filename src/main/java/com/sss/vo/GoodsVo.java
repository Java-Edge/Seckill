package com.sss.vo;

import com.sss.domain.Goods;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 合并两个表数据
 * @author v_shishusheng
 * @date 2018/2/1
 */
@Setter
@Getter
public class GoodsVo extends Goods {
    private Double secKillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
