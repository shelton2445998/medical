package com.fourth.medical.medical.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 体检报告检查项明细
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@TableName("report_item_detail")
@Schema(description = "体检报告检查项明细")
public class ReportItemDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "报告ID")
    private Long reportId;

    @Schema(description = "检查项明细ID")
    private Long detailId;

    @Schema(description = "检查值")
    private String value;

    @Schema(description = "是否异常 0:正常，1:异常")
    private Integer isAbnormal;

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

