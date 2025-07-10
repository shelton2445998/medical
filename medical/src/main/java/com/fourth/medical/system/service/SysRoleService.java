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
