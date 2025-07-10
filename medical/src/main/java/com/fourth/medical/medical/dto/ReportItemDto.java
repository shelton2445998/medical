package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 体检报告检查项信息参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "体检报告检查项信息参数")
public class ReportItemDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Schema(description = "检查项ID")
    @NotNull(message = "检查项ID不能为空")
    private Long itemId;

    @Schema(description = "医生ID")
    @NotNull(message = "医生ID不能为空")
    private Long doctorId;

    @Schema(description = "报告状态 0:未生成，1:已生成")
    private Integer reportStatus;

    @Schema(description = "检查结论")
    private String conclusion;

}


