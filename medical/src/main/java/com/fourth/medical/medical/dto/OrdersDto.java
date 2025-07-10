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
 * 体检预约订单参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "体检预约订单参数")
public class OrdersDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "套餐ID")
    @NotNull(message = "套餐ID不能为空")
    private Long setmealId;

    @Schema(description = "医院ID")
    @NotNull(message = "医院ID不能为空")
    private Long hospitalId;

    @Schema(description = "医生ID")
    @NotNull(message = "医生ID不能为空")
    private Long doctorId;

    @Schema(description = "预约日期")
    @NotNull(message = "预约日期不能为空")
    private Date appointmentDate;

    @Schema(description = "时间段")
    @NotBlank(message = "时间段不能为空")
    @Length(max = 20, message = "时间段长度超过限制")
    private String timeSlot;

    @Schema(description = "状态 0:已取消，1:待支付，2:已支付，3:已完成")
    private Integer status;

    @Schema(description = "订单金额")
    private BigDecimal amount;

    @Schema(description = "支付时间")
    private Date payTime;

    @Schema(description = "支付方式 1:支付宝，2:微信")
    private Integer payType;

    @Schema(description = "支付交易号")
    private String transactionId;

}


