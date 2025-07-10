package com.fourth.medical.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 检查项查询结果
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "检查项查询结果")
public class CheckitemVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "检查项名称")
    private String name;

    @Schema(description = "科室ID")
    private Long departmentId;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "状态 0:禁用，1:启用")
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

