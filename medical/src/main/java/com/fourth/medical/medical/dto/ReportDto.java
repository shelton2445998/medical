package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 体检报告总参数
 *
 * @author fourth
 * @since 2025-07-14
 */
@Data
@Schema(description = "体检报告总参数")
public class ReportDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "检查项ID列表，多个ID用逗号分隔")
    private String checkitemIds;

    @Schema(description = "报告项ID列表，多个ID用逗号分隔")
    private String reportItemIds;

    @Schema(description = "状态 0:未完成，1:已完成")
    private Integer status;

    @Schema(description = "总结论")
    private String conclusion;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "报告日期")
    private Date reportDate;

}


