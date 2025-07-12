package com.fourth.medical.medical.query;

import com.fourth.medical.framework.page.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * App体检报告检查项信息查询参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "App体检报告检查项信息查询参数")
public class AppReportItemQuery extends BasePageQuery {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "用户ID")
    private Long userId;

}

