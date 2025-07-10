package com.fourth.medical.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * App医生查询结果
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "App医生查询结果")
public class AppDoctorVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "医生姓名")
    private String name;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "盐值")
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

    @Schema(description = "创建人ID")
    private Long createId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改人ID")
    private Long updateId;

    @Schema(description = "修改时间")
    private Date updateTime;

}
