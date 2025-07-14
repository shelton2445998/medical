package com.fourth.medical.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fourth.medical.framework.page.BasePageQuery;

/**
 * 分页工具类
 *
 * @author fourth
 * @date 2025/07/21
 **/
public class PageUtil {

    /**
     * 构建MyBatis-Plus分页对象
     *
     * @param query 查询参数
     * @param <T> 查询参数类型
     * @return MyBatis-Plus分页对象
     */
    public static <T> IPage<T> buildPage(BasePageQuery query) {
        if (query == null) {
            return new Page<>(1, 10);
        }
        
        Integer pageIndex = query.getPageIndex();
        Integer pageSize = query.getPageSize();
        
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        
        return new Page<>(pageIndex, pageSize);
    }
} 