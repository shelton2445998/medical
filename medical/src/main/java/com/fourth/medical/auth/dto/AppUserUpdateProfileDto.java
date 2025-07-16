package com.fourth.medical.auth.dto;

import lombok.Data;

/**
 * App用户更新个人信息DTO
 */
@Data
public class AppUserUpdateProfileDto {
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 头像
     */
    private String head;
    
    /**
     * 微信openid
     */
    private String openid;
    
    /**
     * 备注
     */
    private String remark;

    /**
     * 性别 0:女，1:男
     */
    private Integer gender;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 个人简介
     */
    private String introduction;
} 