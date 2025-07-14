package com.fourth.medical.medical.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 体检报告总
 *
 * @author fourth
 * @since 2025-07-14
 */
@Data
@TableName("report")
@Schema(description = "体检报告总")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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

