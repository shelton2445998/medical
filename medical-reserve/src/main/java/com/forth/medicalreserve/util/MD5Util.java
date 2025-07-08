package com.forth.medicalreserve.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 */
public class MD5Util {

    /**
     * MD5加密
     * @param str 需要加密的字符串
     * @return 加密后的字符串
     */
    public static String encrypt(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密出错", e);
        }
    }
    
    /**
     * 验证密码是否正确
     * @param password 明文密码
     * @param md5Password 加密后的密码
     * @return 是否匹配
     */
    public static boolean verify(String password, String md5Password) {
        return encrypt(password).equals(md5Password);
    }
} 