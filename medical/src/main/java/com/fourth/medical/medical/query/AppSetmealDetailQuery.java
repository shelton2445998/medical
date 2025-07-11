package com.fourth.medical.medical.query;

import com.fourth.medical.framework.page.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * App体检套餐明细查询参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "App体检套餐明细查询参数")
public class AppSetmealDetailQuery extends BasePageQuery {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "套餐ID")
    private Long setmealId;
    
    @Schema(description = "所属医院ID")
    private Long hospitalId;
    
    @Schema(description = "所属科室ID")
    private Long departmentId;
    
    @Schema(description = "套餐类型：1-基础，2-高级，3-专项")
    private Integer packageType;
    
    @Schema(description = "状态：0-下架，1-上架")
    private Integer status;
    
    @Schema(description = "关键字搜索")
    private String keyword;
}

