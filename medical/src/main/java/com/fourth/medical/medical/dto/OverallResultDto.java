package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 总检结论参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "总检结论参数")
public class OverallResultDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Schema(description = "结论内容")
    private String content;

    @Schema(description = "医生ID")
    @NotNull(message = "医生ID不能为空")
    private Long doctorId;

}


