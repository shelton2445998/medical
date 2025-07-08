package com.forth.medicalreserve.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 登录请求DTO
 */
@Data
public class LoginRequest implements Serializable {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 用户类型：USER-普通用户，ADMIN-管理员，DOCTOR-医生
     */
    @NotBlank(message = "用户类型不能为空")
    private String userType;
} 