package com.fourth.medical.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 体检报告总查询结果
 *
 * @author fourth
 * @since 2025-07-14
 */
@Data
@Schema(description = "体检报告总查询结果")
public class ReportVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "检查项ID列表，多个ID用逗号分隔")
    private String checkitemIds;

    @Schema(description = "报告项ID列表，多个ID用逗号分隔")
    private String reportItemIds;

    @Schema(description = "状态 0:未完成，1:已完成")
    private Integer status;

    @Schema(description = "总结论")
    private String conclusion;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "报告日期")
    private Date reportDate;

    @Schema(description = "创建人ID")
    private Long createId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改人ID")
    private Long updateId;

    @Schema(description = "修改时间")
    private Date updateTime;

}

