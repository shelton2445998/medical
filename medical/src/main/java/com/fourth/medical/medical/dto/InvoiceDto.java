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
 * 电子发票参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "电子发票参数")
public class InvoiceDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Schema(description = "发票抬头")
    @NotBlank(message = "发票抬头不能为空")
    @Length(max = 100, message = "发票抬头长度超过限制")
    private String title;

    @Schema(description = "税号")
    private String taxNumber;

    @Schema(description = "发票金额")
    @NotNull(message = "发票金额不能为空")
    private BigDecimal amount;

    @Schema(description = "发票类型 1:个人，2:企业")
    @NotNull(message = "发票类型不能为空")
    private Integer type;

    @Schema(description = "发票内容")
    private String content;

    @Schema(description = "状态 0:未开，1:已开")
    private Integer status;

    @Schema(description = "PDF文件地址")
    private String pdfUrl;

}


