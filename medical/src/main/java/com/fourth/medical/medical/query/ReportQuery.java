package com.fourth.medical.medical.query;

import com.fourth.medical.framework.page.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 体检报告总查询参数
 *
 * @author fourth
 * @since 2025-07-14
 */
@Data
@Schema(description = "体检报告总查询参数")
public class ReportQuery extends BasePageQuery {

    private static final long serialVersionUID = 1L;

}

