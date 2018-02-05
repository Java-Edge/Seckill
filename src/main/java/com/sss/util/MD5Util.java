package com.sss.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    //客户端的固定salt
    private static final String salt = "1a2b3c4d";

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    /**
     * 第一层MD5
     * @param inputPass 用户输入的明文密码
     * @return
     */
    public static String inputPassToFormPass(String inputPass) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
        System.out.println(str);
        return md5(str);
    }

    /**
     * 第二层MD5
     * @param formPass 第一层MD5后的表单密码
     * @param salt
     * @return
     */
    public static String formPassToDBPass(String formPass, String salt) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }


    /**
     * 将用户输入的密码转化为数据库存储的密码
     * @param inputPass 用户输入的明文密码
     * @return
     */
    public static String inputPassToDbPass(String inputPass, String saltDB) {
        //将明文密码转为数据库密码
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }
}
