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
 * 检查项参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "检查项参数")
public class CheckitemDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "检查项名称")
    @NotBlank(message = "检查项名称不能为空")
    @Length(max = 100, message = "检查项名称长度超过限制")
    private String name;

    @Schema(description = "科室ID")
    private Long departmentId;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "状态 0:禁用，1:启用")
    private Boolean status;

}


