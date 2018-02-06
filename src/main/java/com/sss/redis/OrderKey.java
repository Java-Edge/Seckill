package com.sss.redis;

/**
 * @author v_shishusheng
 * @date 2018/2/5
 */
public class OrderKey extends BasePrefix {

    public OrderKey(String prefix) {
        super(prefix);
    }
    public static OrderKey getSecKillOrderByUidGid = new OrderKey("moug");
}

