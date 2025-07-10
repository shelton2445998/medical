package com.fourth.medical.medical.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 体检套餐
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@TableName("setmeal")
@Schema(description = "体检套餐")
public class Setmeal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "套餐名称")
    private String name;

    @Schema(description = "套餐类型 1:基础，2:高级，3:专项")
    private Integer type;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "折扣价")
    private BigDecimal discountPrice;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "状态 0:下架，1:上架")
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

