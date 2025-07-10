package com.fourth.medical.user.query;

import com.fourth.medical.framework.page.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户信息查询参数
 *
 * @author geekidea
 * @since 2023-11-30
 */
@Data
@Schema(description = "用户信息查询参数")
public class UserQuery extends BasePageQuery {
    private static final long serialVersionUID = 1L;

}

