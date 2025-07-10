package com.fourth.medical.medical.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 家庭成员
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@TableName("family_member")
@Schema(description = "家庭成员")
public class FamilyMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "关联用户ID")
    private Long userId;

    @Schema(description = "成员姓名")
    private String name;

    @Schema(description = "性别 0:女，1:男")
    private Boolean gender;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "与用户关系")
    private String relation;

    @Schema(description = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createId;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "修改人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateId;

    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

}

