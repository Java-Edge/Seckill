package com.sss.vo;

/**
 * @author v_shishusheng
 * @date 2018/2/5
 */

import com.sss.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsDetailVo {
    private int secKillStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods;
    private User user;
}
