package com.fourth.medical.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * APP用户注册DTO
 *
 * @author fourth
 * @date 2025-07-15
 **/
@Data
@Schema(description = "APP用户注册DTO")
public class AppRegisterDto {

    @Schema(description = "用户名", example = "user123")
    @NotBlank(message = "用户名不能为空")
    @Length(min = 5, message = "用户名长度不能少于5个字符")
    private String username;

    @Schema(description = "密码", example = "123456")
    @NotBlank(message = "密码不能为空")
    private String password;
} 