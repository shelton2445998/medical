package com.fourth.medical.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * App体检预约订单查询结果
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "App体检预约订单查询结果")
public class AppOrdersVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "套餐ID")
    private Long setmealId;

    @Schema(description = "医院ID")
    private Long hospitalId;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "预约日期")
    private Date appointmentDate;

    @Schema(description = "时间段")
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

    @Schema(description = "创建人ID")
    private Long createId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改人ID")
    private Long updateId;

    @Schema(description = "修改时间")
    private Date updateTime;

}
