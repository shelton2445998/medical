package com.fourth.medical.medical.query;

import com.fourth.medical.framework.page.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 检查项查询参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "检查项查询参数")
public class CheckitemQuery extends BasePageQuery {

    private static final long serialVersionUID = 1L;

}

