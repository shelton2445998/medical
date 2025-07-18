package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 医生修改个人信息DTO
 *
 * @author fourth
 * @since 2025-07-18
 */
@Data
@Schema(description = "医生修改个人信息DTO")
public class DoctorUpdateProfileDto {

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "职称")
    private String title;

    @Schema(description = "专业特长")
    private String specialty;

    @Schema(description = "简介")
    private String introduction;
} 