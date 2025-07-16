package com.fourth.medical.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * App端修改用户个人信息参数
 *
 * @author geekidea
 * @since 2023-11-30
 */
@Data
@Schema(description = "App端修改用户个人信息参数")
public class AppUserUpdateProfileDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "昵称")
    @Length(max = 20, message = "昵称长度超过限制")
    private String nickname;

    @Schema(description = "手机号码")
    @Length(max = 11, message = "手机号码长度超过限制")
    private String phone;

    @Schema(description = "头像")
    @Length(max = 200, message = "头像长度超过限制")
    private String head;

    @Schema(description = "微信openid")
    @Length(max = 200, message = "微信openid长度超过限制")
    private String openid;

    @Schema(description = "备注")
    @Length(max = 500, message = "备注长度超过限制")
    private String remark;

    @Schema(description = "性别 0:女，1:男")
    private Integer gender;

    @Schema(description = "身份证号")
    @Length(max = 18, message = "身份证号长度超过限制")
    private String idCard;

    @Schema(description = "个人简介")
    @Length(max = 200, message = "个人简介长度超过限制")
    private String introduction;

} 