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
    private Long setmealId;

    @Schema(description = "医院ID")
    @NotNull(message = "医院ID不能为空")
    private Long hospitalId;

    @Schema(description = "医生ID")
    @NotNull(message = "医生ID不能为空")
    private Long doctorId;

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
    
    @Schema(description = "检查项ID列表，多个ID用逗号分隔")
    private String checkitemIds;

    @Schema(description = "患者姓名")
    private String patientName;

    @Schema(description = "患者年龄")
    private Integer patientAge;

    @Schema(description = "患者性别 0:女，1:男")
    private Integer patientGender;

    @Schema(description = "患者手机号")
    private String patientPhone;

} 