package com.fourth.medical.medical.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 电子发票
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@TableName("invoice")
@Schema(description = "电子发票")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "发票抬头")
    private String title;

    @Schema(description = "税号")
    private String taxNumber;

    @Schema(description = "发票金额")
    private BigDecimal amount;

    @Schema(description = "发票类型 1:个人，2:企业")
    private Integer type;

    @Schema(description = "发票内容")
    private String content;

    @Schema(description = "状态 0:未开，1:已开")
    private Integer status;

    @Schema(description = "PDF文件地址")
    private String pdfUrl;

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

