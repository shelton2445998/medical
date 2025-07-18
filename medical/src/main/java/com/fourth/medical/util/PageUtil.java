package com.fourth.medical.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fourth.medical.framework.page.BasePageQuery;

/**
 * 分页工具类
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供统一的分页参数构建和验证功能</li>
 *   <li>将前端分页查询参数转换为MyBatis-Plus分页对象</li>
 *   <li>确保分页参数的合法性和默认值处理</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>自动参数验证：检查页码和页大小的有效性</li>
 *   <li>默认值处理：提供合理的默认分页参数</li>
 *   <li>类型安全：使用泛型确保类型安全</li>
 *   <li>空值处理：优雅处理null参数情况</li>
 * </ul>
 * 
 * <p><strong>使用场景：</strong></p>
 * <ul>
 *   <li>Controller层接收分页查询请求</li>
 *   <li>Service层构建分页查询条件</li>
 *   <li>统一分页参数处理逻辑</li>
 * </ul>
 * 
 * <p><strong>技术实现：</strong></p>
 * <ul>
 *   <li>基于MyBatis-Plus分页插件</li>
 *   <li>支持BasePageQuery查询参数</li>
 *   <li>静态工具方法设计</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>可扩展支持更多分页参数类型</li>
 *   <li>可添加分页参数验证规则</li>
 *   <li>可支持自定义默认分页大小</li>
 * </ul>
 *
 * @author fourth
 * @date 2025/07/21
 **/
public class PageUtil {

    /**
     * 构建MyBatis-Plus分页对象
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>将BasePageQuery查询参数转换为MyBatis-Plus的IPage对象</li>
     *   <li>自动处理页码和页大小的验证和默认值</li>
     *   <li>确保返回有效的分页对象</li>
     * </ul>
     * 
     * <p><strong>参数处理逻辑：</strong></p>
     * <ul>
     *   <li>query为null时：返回默认分页对象（第1页，每页10条）</li>
     *   <li>pageIndex无效时：设置为1</li>
     *   <li>pageSize无效时：设置为10</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * BasePageQuery query = new BasePageQuery();
     * query.setPageIndex(2);
     * query.setPageSize(20);
     * IPage<User> page = PageUtil.buildPage(query);
     * }</pre>
     *
     * @param query 查询参数，包含分页信息
     * @param <T> 查询参数类型
     * @return MyBatis-Plus分页对象，包含有效的分页参数
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