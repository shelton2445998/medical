package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 体检报告检查项明细参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "体检报告检查项明细参数")
public class ReportItemDetailDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "报告ID")
    @NotNull(message = "报告ID不能为空")
    private Long reportId;

    @Schema(description = "检查项明细ID")
    @NotNull(message = "检查项明细ID不能为空")
    private Long detailId;

    @Schema(description = "检查值")
    private String value;

    @Schema(description = "是否异常 0:正常，1:异常")
    private Integer isAbnormal;

}


