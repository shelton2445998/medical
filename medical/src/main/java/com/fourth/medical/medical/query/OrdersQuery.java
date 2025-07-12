package com.fourth.medical.medical.query;

import com.fourth.medical.framework.page.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 体检预约订单查询参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "体检预约订单查询参数")
public class OrdersQuery extends BasePageQuery {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "套餐ID")
    private Long setmealId;
    
    @Schema(description = "医院ID")
    private Long hospitalId;
    
    @Schema(description = "医生ID")
    private Long doctorId;
    
    @Schema(description = "状态 0:已取消，1:待支付，2:已支付，3:已完成")
    private Integer status;
    
    @Schema(description = "预约起始日期")
    private Date startDate;
    
    @Schema(description = "预约结束日期")
    private Date endDate;
}

