package com.forth.medicalreserve.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse implements Serializable {

    /**
     * 用户ID
     */
    private Integer id;
    
    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 角色/用户类型
     */
    private String role;

    /**
     * 访问令牌
     */
    private String token;
} 