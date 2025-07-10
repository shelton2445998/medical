package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 家庭成员参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "家庭成员参数")
public class FamilyMemberDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "关联用户ID")
    @NotNull(message = "关联用户ID不能为空")
    private Long userId;

    @Schema(description = "成员姓名")
    @NotBlank(message = "成员姓名不能为空")
    @Length(max = 32, message = "成员姓名长度超过限制")
    private String name;

    @Schema(description = "性别 0:女，1:男")
    private Boolean gender;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "与用户关系")
    private String relation;

}


