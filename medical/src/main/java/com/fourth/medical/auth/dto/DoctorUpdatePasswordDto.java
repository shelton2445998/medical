package com.fourth.medical.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 医生修改密码DTO
 *
 * @author fourth
 * @since 2025-07-18
 */
@Data
@Schema(description = "医生修改密码DTO")
public class DoctorUpdatePasswordDto {

    @NotBlank(message = "原密码不能为空")
    @Schema(description = "原密码")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码")
    private String newPassword;
} 