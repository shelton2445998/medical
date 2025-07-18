package com.fourth.medical.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 系统用户修改个人信息DTO
 *
 * @author fourth
 * @since 2025-07-18
 */
@Data
@Schema(description = "系统用户修改个人信息DTO")
public class SysUserUpdateProfileDto {

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像")
    private String head;
}


