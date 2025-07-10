package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 检查项明细参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "检查项明细参数")
public class CheckitemDetailDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "检查项ID")
    @NotNull(message = "检查项ID不能为空")
    private Long itemId;

    @Schema(description = "明细名称")
    @NotBlank(message = "明细名称不能为空")
    @Length(max = 100, message = "明细名称长度超过限制")
    private String name;

    @Schema(description = "正常值范围")
    private String normalValue;

    @Schema(description = "单位")
    private String unit;

}


