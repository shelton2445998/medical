package com.fourth.medical.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.system.dto.RoleMenusDto;
import com.fourth.medical.system.dto.SysRoleDto;
import com.fourth.medical.system.entity.SysRole;
import com.fourth.medical.system.query.SysRoleQuery;
import com.fourth.medical.system.vo.SysRoleVo;

import java.util.List;

/**
 * 系统角色 服务接口
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供系统角色管理的核心业务逻辑，包括角色的增删改查、权限分配、角色用户管理等</li>
 *   <li>支持系统角色的完整生命周期管理，为权限系统提供角色数据服务</li>
 *   <li>提供基于RBAC模型的角色权限管理</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>角色管理：支持系统角色的创建、编辑、删除和查询</li>
 *   <li>权限分配：支持角色与菜单权限的关联分配</li>
 *   <li>角色用户：支持角色与用户的关联管理</li>
 *   <li>分页查询：支持大数据量的分页查询优化</li>
 *   <li>唯一性检查：确保角色编码的唯一性</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>角色创建：管理员创建新的系统角色</li>
 *   <li>权限分配：为角色分配相应的菜单权限</li>
 *   <li>用户关联：将用户与角色进行关联</li>
 *   <li>角色维护：根据业务需求更新角色信息</li>
 *   <li>权限验证：验证角色的权限配置</li>
 * </ol>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>权限控制：基于角色的数据访问控制</li>
 *   <li>数据验证：对输入数据进行严格验证</li>
 *   <li>唯一性检查：确保角色编码的唯一性</li>
 *   <li>关联保护：保护角色与权限、用户关联关系的完整性</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>角色继承：可扩展支持角色继承功能</li>
 *   <li>动态权限：可扩展支持动态权限分配</li>
 *   <li>角色审计：可扩展支持角色变更审计功能</li>
 *   <li>权限分析：可扩展支持权限使用分析</li>
 * </ul>
 *
 * @author geekidea
 * @since 2022-12-26
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 添加系统角色
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addSysRole(SysRoleDto dto);

    /**
     * 修改系统角色
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateSysRole(SysRoleDto dto);

    /**
     * 删除系统角色
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSysRole(Long id);

    /**
     * 系统角色详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    SysRoleVo getSysRoleById(Long id);

    /**
     * 系统角色分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<SysRoleVo> getSysRolePage(SysRoleQuery query);

    /**
     * 获取所有角色列表
     *
     * @return
     * @throws Exception
     */
    List<SysRole> getSysRoleAllList();

    /**
     * 设置角色权限
     *
     * @param roleMenusDto
     * @return
     * @throws Exception
     */
    boolean setRoleMenus(RoleMenusDto roleMenusDto);

    /**
     * 检查code是否存在
     *
     * @param code
     * @return
     * @throws Exception
     */
    void checkCodeExists(String code);

}
