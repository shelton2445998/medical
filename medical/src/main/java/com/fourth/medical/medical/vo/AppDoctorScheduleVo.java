package com.fourth.medical.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * App医生排班查询结果
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "App医生排班查询结果")
public class AppDoctorScheduleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
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
    private Long createId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改人ID")
    private Long updateId;

    @Schema(description = "修改时间")
    private Date updateTime;

}
