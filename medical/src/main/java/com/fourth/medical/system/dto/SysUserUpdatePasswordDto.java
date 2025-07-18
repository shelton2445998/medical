package com.fourth.medical.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 系统用户修改密码DTO
 *
 * @author fourth
 * @since 2025-07-18
 */
@Data
@Schema(description = "系统用户修改密码DTO")
public class SysUserUpdatePasswordDto {

    @Schema(description = "用户ID")
    private String id;

    @NotBlank(message = "原密码不能为空")
    @Schema(description = "原密码")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    @Schema(description = "确认密码")
    private String confirmPassword;
}
