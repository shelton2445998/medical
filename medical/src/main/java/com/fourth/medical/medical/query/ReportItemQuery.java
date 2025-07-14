package com.fourth.medical.medical.query;

import com.fourth.medical.framework.page.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 体检报告检查项信息查询参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "体检报告检查项信息查询参数")
public class ReportItemQuery extends BasePageQuery {

    private static final long serialVersionUID = 1L;

    @Schema(description = "医生ID")
    private Long doctorId;
    
    @Schema(description = "订单ID")
    private Long orderId;
    
    @Schema(description = "患者关键词(姓名或手机号)")
    private String patientKeyword;
    
    @Schema(description = "报告状态 0:未生成，1:已生成")
    private Integer reportStatus;
}

