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
 * 体检套餐参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "体检套餐参数")
public class SetmealDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "套餐名称")
    @NotBlank(message = "套餐名称不能为空")
    @Length(max = 100, message = "套餐名称长度超过限制")
    private String name;

    @Schema(description = "套餐类型 1:基础，2:高级，3:专项")
    @NotNull(message = "套餐类型不能为空")
    private Integer type;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "折扣价")
    private BigDecimal discountPrice;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "状态 0:下架，1:上架")
    private Boolean status;

}


