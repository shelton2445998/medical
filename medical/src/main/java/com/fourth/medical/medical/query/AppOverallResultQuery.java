package com.fourth.medical.medical.query;

import com.fourth.medical.framework.page.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * App总检结论查询参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "App总检结论查询参数")
public class AppOverallResultQuery extends BasePageQuery {

    private static final long serialVersionUID = 1L;

}

