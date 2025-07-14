package com.fourth.medical.medical.query;

import com.fourth.medical.framework.page.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * App体检报告总查询参数
 *
 * @author fourth
 * @since 2025-07-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "App体检报告总查询参数")
public class AppReportQuery extends BasePageQuery {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 订单ID
     */
    @Schema(description = "订单ID")
    private Long orderId;
    
    /**
     * 状态 0:未完成，1:已完成
     */
    @Schema(description = "状态 0:未完成，1:已完成")
    private Integer status;
}

