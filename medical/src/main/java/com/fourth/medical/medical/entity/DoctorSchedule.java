package com.fourth.medical.medical.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 医生排班
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@TableName("doctor_schedule")
@Schema(description = "医生排班")
public class DoctorSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "工作日期")
    private Date workDate;

    @Schema(description = "时间段")
    private String timeSlot;

    @Schema(description = "最大可预约人数")
    private Integer maxNumber;

    @Schema(description = "已预约人数")
    private Integer reservedNumber;

    @Schema(description = "状态 0:停诊，1:正常")
    private Boolean status;

    @Schema(description = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createId;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "修改人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateId;

    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

}

