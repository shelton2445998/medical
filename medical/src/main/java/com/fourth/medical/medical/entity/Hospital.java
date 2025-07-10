package com.fourth.medical.medical.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 医院
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@TableName("hospital")
@Schema(description = "医院")
public class Hospital implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "医院名称")
    private String name;

    @Schema(description = "医院电话")
    private String phone;

    @Schema(description = "医院地址")
    private String address;

    @Schema(description = "医院简介")
    private String description;

    @Schema(description = "状态 0:禁用，1:启用")
    private Boolean status;

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

