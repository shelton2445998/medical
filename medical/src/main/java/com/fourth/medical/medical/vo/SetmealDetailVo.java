package com.fourth.medical.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 体检套餐明细查询结果
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "体检套餐明细查询结果")
public class SetmealDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "套餐ID")
    private Long setmealId;

    @Schema(description = "检查项ID")
    private Long itemId;

    @Schema(description = "创建人ID")
    private Long createId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改人ID")
    private Long updateId;

    @Schema(description = "修改时间")
    private Date updateTime;

}

