package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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

    @Schema(description = "检查项ID")
    @NotNull(message = "检查项ID不能为空")
    private Long itemId;

}


