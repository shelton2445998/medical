package com.fourth.medical.medical.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 体检报告检查项信息
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@TableName("report_item")
@Schema(description = "体检报告检查项信息")
public class ReportItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "检查项ID")
    private Long itemId;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "报告状态 0:未生成，1:已生成")
    private Integer reportStatus;

    @Schema(description = "检查结论")
    private String conclusion;

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

