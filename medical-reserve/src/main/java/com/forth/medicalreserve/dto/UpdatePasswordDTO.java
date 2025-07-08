package com.forth.medicalreserve.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 修改密码DTO
 */
@Data
public class UpdatePasswordDTO {

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String newPassword;

    /**
     * 确认新密码
     */
    @NotBlank(message = "确认新密码不能为空")
    private String confirmPassword;
} 