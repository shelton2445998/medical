package com.fourth.medical.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 检查项明细查询结果
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "检查项明细查询结果")
public class CheckitemDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "检查项ID")
    private Long itemId;

    @Schema(description = "明细名称")
    private String name;

    @Schema(description = "正常值范围")
    private String normalValue;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "创建人ID")
    private Long createId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改人ID")
    private Long updateId;

    @Schema(description = "修改时间")
    private Date updateTime;

}

