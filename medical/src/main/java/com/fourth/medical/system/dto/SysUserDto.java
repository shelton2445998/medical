package com.fourth.medical.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 修改系统用户参数
 *
 * @author geekidea
 * @since 2022-12-26
 */
@Data
@Schema(description = "系统用户参数")
public class SysUserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名长度超过限制")
    private String username;

    @Schema(description = "昵称")
    @NotBlank(message = "昵称不能为空")
    @Length(max = 20, message = "昵称长度超过限制")
    private String nickname;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "手机号码")
    @Length(max = 11, message = "手机号码长度超过限制")
    private String phone;

    @Schema(description = "电子邮件")
    @Length(max = 255, message = "电子邮件长度超过限制")
    private String email;

    @Schema(description = "状态，0：禁用，1：启用")
    private Boolean status;

    @Schema(description = "角色id")
    private Long roleId;

}


