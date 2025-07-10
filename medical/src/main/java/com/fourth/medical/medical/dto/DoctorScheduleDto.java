package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 医生排班参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "医生排班参数")
public class DoctorScheduleDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "医生ID")
    @NotNull(message = "医生ID不能为空")
    private Long doctorId;

    @Schema(description = "工作日期")
    @NotNull(message = "工作日期不能为空")
    private Date workDate;

    @Schema(description = "时间段")
    @NotBlank(message = "时间段不能为空")
    @Length(max = 20, message = "时间段长度超过限制")
    private String timeSlot;

    @Schema(description = "最大可预约人数")
    private Integer maxNumber;

    @Schema(description = "已预约人数")
    private Integer reservedNumber;

    @Schema(description = "状态 0:停诊，1:正常")
    private Boolean status;

}


