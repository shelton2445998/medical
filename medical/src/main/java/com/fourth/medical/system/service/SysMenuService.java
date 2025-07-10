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
