package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * App体检预约订单DTO
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "App体检预约订单DTO")
public class AppOrdersDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "套餐ID")
    @NotNull(message = "套餐ID不能为空")
    private Long setmealId;

    @Schema(description = "医院ID")
    @NotNull(message = "医院ID不能为空")
    private Long hospitalId;

    @Schema(description = "家庭成员ID")
    @NotNull(message = "家庭成员ID不能为空")
    private Long familyMemberId;

    @Schema(description = "预约日期")
    @NotNull(message = "预约日期不能为空")
    private Date appointmentDate;

    @Schema(description = "预约时间段")
    @NotNull(message = "预约时间段不能为空")
    private String appointmentTime;

    @Schema(description = "备注")
    private String remark;

} 