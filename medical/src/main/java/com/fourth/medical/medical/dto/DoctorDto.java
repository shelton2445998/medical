package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 医生参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "医生参数")
public class DoctorDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "医生姓名")
    @NotBlank(message = "医生姓名不能为空")
    @Length(max = 32, message = "医生姓名长度超过限制")
    private String name;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    @Length(max = 64, message = "密码长度超过限制")
    private String password;

    @Schema(description = "盐值")
    @Length(max = 32, message = "盐值长度超过限制")
    private String salt;

    @Schema(description = "性别 0:女，1:男")
    private Boolean gender;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "所属医院ID")
    private Long hospitalId;

    @Schema(description = "科室ID")
    private Long departmentId;

    @Schema(description = "职称")
    private String title;

    @Schema(description = "简介")
    private String introduction;

    @Schema(description = "状态 0:禁用，1:启用")
    private Boolean status;

}


