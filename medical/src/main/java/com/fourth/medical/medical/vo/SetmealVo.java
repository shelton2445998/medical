package com.fourth.medical.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 体检套餐查询结果
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "体检套餐查询结果")
public class SetmealVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
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
    private Long createId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改人ID")
    private Long updateId;

    @Schema(description = "修改时间")
    private Date updateTime;

}

