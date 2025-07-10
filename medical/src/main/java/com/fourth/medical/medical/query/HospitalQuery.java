package com.fourth.medical.medical.query;

import com.fourth.medical.framework.page.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 医院查询参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "医院查询参数")
public class HospitalQuery extends BasePageQuery {

    private static final long serialVersionUID = 1L;

}

