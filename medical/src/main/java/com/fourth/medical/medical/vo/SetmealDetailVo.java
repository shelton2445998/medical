package com.fourth.medical.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 体检套餐明细查询结果
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "体检套餐明细查询结果")
public class SetmealDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
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
    private Long createId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改人ID")
    private Long updateId;

    @Schema(description = "修改时间")
    private Date updateTime;

}

