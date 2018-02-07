package com.sss.redis;

/**
 * @author v_shishusheng
 * @date 2018/2/7
 */
public class SecKillKey extends BasePrefix{

    private SecKillKey(String prefix) {
        super(prefix);
    }
    public static SecKillKey isGoodsOver = new SecKillKey("go");

}
