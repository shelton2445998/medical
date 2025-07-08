package com.forth.medicalreserve.constant;

/**
 * 公共常量类
 */
public class CommonConstant {

    /**
     * 用户登录token前缀
     */
    public static final String TOKEN_PREFIX = "user_token:";
    
    /**
     * 管理员登录token前缀
     */
    public static final String ADMIN_TOKEN_PREFIX = "admin_token:";
    
    /**
     * 医生登录token前缀
     */
    public static final String DOCTOR_TOKEN_PREFIX = "doctor_token:";
    
    /**
     * token过期时间（小时）
     */
    public static final int TOKEN_EXPIRE_HOURS = 24;
    
    /**
     * 成功状态码
     */
    public static final int SUCCESS_CODE = 200;
    
    /**
     * 失败状态码
     */
    public static final int ERROR_CODE = 500;
    
    /**
     * 未授权状态码
     */
    public static final int UNAUTHORIZED_CODE = 401;
} 