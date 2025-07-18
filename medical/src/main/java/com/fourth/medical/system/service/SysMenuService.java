package com.fourth.medical.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.system.dto.SysMenuDto;
import com.fourth.medical.system.entity.SysMenu;
import com.fourth.medical.system.query.SysMenuQuery;
import com.fourth.medical.system.vo.SysMenuTreeVo;
import com.fourth.medical.system.vo.SysMenuVo;
import com.fourth.medical.system.vo.SysNavMenuTreeVo;

import java.util.List;

/**
 * 系统菜单 服务接口
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供系统菜单管理的核心业务逻辑，包括菜单的增删改查、树形结构管理、权限分配等</li>
 *   <li>支持系统菜单的完整生命周期管理，为前端导航提供菜单数据服务</li>
 *   <li>提供基于角色的菜单权限控制和动态导航生成</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>菜单管理：支持系统菜单的创建、编辑、删除和查询</li>
 *   <li>树形结构：支持多级菜单的树形结构管理</li>
 *   <li>权限控制：支持基于角色的菜单权限分配</li>
 *   <li>动态导航：支持根据用户权限生成动态导航菜单</li>
 *   <li>状态管理：支持菜单的启用/禁用状态控制</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>菜单创建：管理员创建系统菜单，设置层级关系</li>
 *   <li>权限分配：为角色分配菜单权限</li>
 *   <li>导航生成：根据用户角色生成导航菜单</li>
 *   <li>状态管理：管理菜单的显示状态</li>
 *   <li>结构维护：维护菜单的层级结构</li>
 * </ol>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>权限控制：基于角色的菜单访问控制</li>
 *   <li>数据验证：对输入数据进行严格验证</li>
 *   <li>结构完整性：确保菜单层级结构的完整性</li>
 *   <li>访问控制：确保用户只能访问有权限的菜单</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>菜单缓存：可扩展支持菜单数据的缓存机制</li>
 *   <li>个性化菜单：可扩展支持用户个性化菜单配置</li>
 *   <li>菜单统计：可扩展支持菜单使用统计分析</li>
 *   <li>多语言菜单：可扩展支持多语言菜单管理</li>
 * </ul>
 *
 * @author geekidea
 * @since 2022-12-26
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 添加系统菜单
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addSysMenu(SysMenuDto dto);

    /**
     * 修改系统菜单
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateSysMenu(SysMenuDto dto);

    /**
     * 删除系统菜单
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSysMenu(Long id);

    /**
     * 系统菜单详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    SysMenuVo getSysMenuById(Long id);

    /**
     * 获取所有的系统菜单树形列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    List<SysMenuTreeVo> getAllSysMenuTreeList(SysMenuQuery query);

    /**
     * 获取启用的系统菜单树形列表
     *
     * @return
     * @throws Exception
     */
    List<SysMenuTreeVo> getSysMenuTreeList();

    /**
     * 获取当前登录用户导航菜单树形列表
     *
     * @return
     * @throws Exception
     */
    List<SysNavMenuTreeVo> getNavMenuTreeList();

    /**
     * 获取角色权限ID集合
     *
     * @param roleId
     * @return
     * @throws Exception
     */
    List<Long> getMenuIdsByRoleId(Long roleId);

}
