package com.fourth.medical.medical.query;

import com.fourth.medical.framework.page.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 总检结论查询参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "总检结论查询参数")
public class OverallResultQuery extends BasePageQuery {

    private static final long serialVersionUID = 1L;

}

