package com.fourth.medical.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.system.dto.SysDictDto;
import com.fourth.medical.system.entity.SysDict;
import com.fourth.medical.system.query.SysDictAppQuery;
import com.fourth.medical.system.query.SysDictQuery;
import com.fourth.medical.system.vo.AppSysDictVo;
import com.fourth.medical.system.vo.SysDictVo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 字典数据 服务接口
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供系统字典数据管理的核心业务逻辑，包括字典的增删改查、分类管理、数据转换等</li>
 *   <li>支持系统字典数据的完整生命周期管理，为系统提供统一的数据字典服务</li>
 *   <li>提供移动端和Web端统一的字典数据访问接口</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>字典管理：支持字典数据的创建、编辑、删除和查询</li>
 *   <li>分类管理：支持字典数据的分类和编码管理</li>
 *   <li>数据转换：支持字典编码与标签的双向转换</li>
 *   <li>移动端适配：提供专门的移动端数据格式</li>
 *   <li>分页查询：支持大数据量的分页查询优化</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>字典创建：管理员创建新的字典数据</li>
 *   <li>分类管理：对字典数据进行分类和编码管理</li>
 *   <li>数据维护：根据业务需求更新字典数据</li>
 *   <li>数据查询：支持多种条件的字典数据查询</li>
 *   <li>数据转换：提供编码与标签的转换服务</li>
 * </ol>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>权限控制：基于角色的数据访问控制</li>
 *   <li>数据验证：对输入数据进行严格验证</li>
 *   <li>数据完整性：确保字典数据的准确性和完整性</li>
 *   <li>缓存管理：合理使用缓存提高查询性能</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>字典缓存：可扩展支持字典数据的缓存机制</li>
 *   <li>国际化：可扩展支持多语言字典数据管理</li>
 *   <li>动态更新：可扩展支持字典数据的动态更新</li>
 *   <li>版本控制：可扩展支持字典数据版本管理</li>
 * </ul>
 *
 * @author geekidea
 * @since 2023-11-25
 */
public interface SysDictService extends IService<SysDict> {

    /**
     * 添加字典数据
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addSysDict(SysDictDto dto);

    /**
     * 修改字典数据
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateSysDict(SysDictDto dto);

    /**
     * 删除字典数据
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSysDict(Long id);

    /**
     * 字典数据详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    SysDictVo getSysDictById(Long id);

    /**
     * 字典数据分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<SysDictVo> getSysDictPage(SysDictQuery query);

    /**
     * App字典数据列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Map<String, List<AppSysDictVo>> getAppSysDictList(SysDictAppQuery query);

    /**
     * 根据字典编码获取字典列表
     *
     * @param dictCode
     * @return
     * @throws Exception
     */
    List<SysDict> getSysDictList(String dictCode);

    /**
     * 根据字典编码和label获取字典对象
     *
     * @param dictCode
     * @param value
     * @return
     * @throws Exception
     */
    SysDict getSysDictByValue(String dictCode, Serializable value);

    /**
     * 根据字典编码和label获取字典值
     *
     * @param dictCode
     * @param value
     * @return
     * @throws Exception
     */
    String getSysDictLabelByValue(String dictCode, Serializable value);

}
