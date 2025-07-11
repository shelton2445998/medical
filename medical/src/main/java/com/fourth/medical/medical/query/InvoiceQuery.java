package com.fourth.medical.medical.query;

import com.fourth.medical.framework.page.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 电子发票查询参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "电子发票查询参数")
public class InvoiceQuery extends BasePageQuery {

    private static final long serialVersionUID = 1L;

}

