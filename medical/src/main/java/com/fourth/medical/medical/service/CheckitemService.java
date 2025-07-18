package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.CheckitemDto;
import com.fourth.medical.medical.entity.Checkitem;
import com.fourth.medical.medical.query.CheckitemQuery;
import com.fourth.medical.medical.vo.CheckitemVo;
import com.fourth.medical.medical.query.AppCheckitemQuery;
import com.fourth.medical.medical.vo.AppCheckitemVo;


/**
 * 检查项 服务接口
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供检查项信息管理的核心业务逻辑，包括检查项的增删改查、分类管理、关联查询等</li>
 *   <li>支持检查项信息的完整生命周期管理，为体检套餐配置提供数据支撑</li>
 *   <li>提供移动端和Web端统一的检查项数据访问接口</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>检查项管理：支持检查项信息的创建、编辑、删除和查询</li>
 *   <li>分类管理：支持检查项的分类和标签管理</li>
 *   <li>关联查询：支持检查项与部门的关联查询</li>
 *   <li>分页查询：支持大数据量的分页查询优化</li>
 *   <li>移动端适配：提供专门的移动端数据格式</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>检查项创建：管理员创建新的检查项，设置基本信息</li>
 *   <li>信息维护：根据业务需求更新检查项信息</li>
 *   <li>分类管理：对检查项进行分类和标签管理</li>
 *   <li>关联配置：配置检查项与部门的关联关系</li>
 *   <li>数据查询：支持多种条件的检查项信息查询</li>
 * </ol>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>权限控制：基于角色的数据访问控制</li>
 *   <li>数据验证：对输入数据进行严格验证</li>
 *   <li>数据完整性：确保检查项数据的准确性和完整性</li>
 *   <li>关联保护：保护检查项与部门关联关系的完整性</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>智能推荐：可扩展支持基于用户特征的检查项推荐</li>
 *   <li>模板管理：可扩展支持检查项模板功能</li>
 *   <li>批量操作：可扩展支持批量导入、导出检查项</li>
 *   <li>统计分析：可扩展支持检查项使用统计分析</li>
 * </ul>
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface CheckitemService extends IService<Checkitem> {

    /**
     * 添加检查项
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addCheckitem(CheckitemDto dto);

    /**
     * 修改检查项
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateCheckitem(CheckitemDto dto);

    /**
     * 删除检查项
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteCheckitem(Long id);

    /**
     * 检查项详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    CheckitemVo getCheckitemById(Long id);

    /**
     * 检查项分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<CheckitemVo> getCheckitemPage(CheckitemQuery query);

    /**
     * App检查项详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppCheckitemVo getAppCheckitemById(Long id);

    /**
     * App检查项分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppCheckitemVo> getAppCheckitemPage(AppCheckitemQuery query);

    /**
     * 根据检查项ID列表获取部门信息
     *
     * @param checkitemIds 检查项ID列表，逗号分隔
     * @return 部门信息
     * @throws Exception
     */
    Object getDepartmentByCheckitemIds(String checkitemIds);

}
