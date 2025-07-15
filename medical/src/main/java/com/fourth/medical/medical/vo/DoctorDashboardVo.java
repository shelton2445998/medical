package com.fourth.medical.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 医生仪表盘数据VO
 *
 * @author fourth
 * @since 2023-07-09
 */
@Data
@Schema(description = "医生仪表盘数据")
public class DoctorDashboardVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "待处理报告数")
    private Integer pendingReports;

    @Schema(description = "今日是否有排班")
    private Boolean hasTodaySchedule;

    @Schema(description = "本月体检报告数")
    private Integer monthReports;
} 