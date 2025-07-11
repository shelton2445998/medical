package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 体检套餐明细参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "体检套餐明细参数")
public class SetmealDetailDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "套餐ID")
    @NotNull(message = "套餐ID不能为空")
    private Long setmealId;
    
    @Schema(description = "套餐名称")
    @NotBlank(message = "套餐名称不能为空")
    @Length(max = 100, message = "套餐名称不能超过100个字符")
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
    @NotNull(message = "所属医院ID不能为空")
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
    @NotNull(message = "检查项ID不能为空")
    private Long itemId;

}


