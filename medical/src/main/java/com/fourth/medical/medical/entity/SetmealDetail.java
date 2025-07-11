package com.fourth.medical.medical.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 体检套餐明细
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@TableName("setmeal_detail")
@Schema(description = "体检套餐明细")
public class SetmealDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "套餐ID")
    private Long setmealId;
    
    @Schema(description = "套餐名称")
    private String name;
    
    @Schema(description = "套餐原价")
    private BigDecimal price;
    
    @Schema(description = "优惠价格")
    private BigDecimal discountPrice;
    
    @Schema(description = "套餐详细介绍")
    private String description;
    
    @Schema(description = "检查项目列表")
    private String checkItems;
    
    @Schema(description = "适用人群描述")
    private String suitableCrowd;
    
    @Schema(description = "预约须知")
    private String appointmentNotice;
    
    @Schema(description = "用户评价")
    private String userReviews;
    
    @Schema(description = "所属医院ID")
    private Long hospitalId;
    
    @Schema(description = "所属科室ID")
    private Long departmentId;
    
    @Schema(description = "套餐类型：1-基础，2-高级，3-专项")
    private Integer packageType;
    
    @Schema(description = "状态：0-下架，1-上架")
    private Integer status;
    
    @Schema(description = "排序权重")
    private Integer sortOrder;

    @Schema(description = "检查项ID")
    private Long itemId;

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

