package com.sss.redis;

/**
 * @author v_shishusheng
 * @date 2018/2/7
 */
public class SecKillKey extends BasePrefix{

    private SecKillKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static SecKillKey isGoodsOver = new SecKillKey(0,"go");
    public static SecKillKey getSecKillPath = new SecKillKey(60,"skp");

}
