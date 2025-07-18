package com.fourth.medical.system.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.system.dto.RoleMenusDto;
import com.fourth.medical.system.dto.SysRoleDto;
import com.fourth.medical.system.entity.SysRole;
import com.fourth.medical.system.query.SysRoleQuery;
import com.fourth.medical.system.service.SysMenuService;
import com.fourth.medical.system.service.SysRoleService;
import com.fourth.medical.system.vo.SysRoleVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统角色管理控制器
 * 
 * 功能说明：
 * 本控制器负责管理系统的角色权限体系，包括角色的创建、权限分配、
 * 角色关联管理等功能。角色是权限管理的核心概念，通过角色可以
 * 统一管理用户的权限，实现灵活的权限控制。
 * 
 * 主要功能：
 * 1. 角色基础管理 - 角色的创建、修改、删除
 * 2. 权限分配管理 - 角色与菜单权限的关联
 * 3. 角色用户管理 - 角色与用户的关联关系
 * 4. 角色状态管理 - 角色的启用/禁用控制
 * 5. 角色权限查询 - 角色权限的查询和统计
 * 
 * 业务特点：
 * - 基于RBAC权限模型设计
 * - 支持角色继承和权限组合
 * - 灵活的权限控制粒度
 * - 与用户管理和菜单管理深度集成
 * 
 * @author geekidea
 * @since 2022-12-26
 * @version 1.0
 */
@Slf4j
@RestController
@Tag(name = "系统角色")
@RequestMapping("/admin/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 添加系统角色
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @PostMapping("/addSysRole")
    @Operation(summary = "添加系统角色")
    @Permission("sys:role:add")
    public ApiResult addSysRole(@Valid @RequestBody SysRoleDto dto) {
        boolean flag = sysRoleService.addSysRole(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改系统角色
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @PostMapping("/updateSysRole")
    @Operation(summary = "修改系统角色")
    @Permission("sys:role:update")
    public ApiResult updateSysRole(@Valid @RequestBody SysRoleDto dto) {
        boolean flag = sysRoleService.updateSysRole(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除系统角色
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/deleteSysRole/{id}")
    @Operation(summary = "删除系统角色")
    @Permission("sys:role:delete")
    public ApiResult deleteSysRole(@PathVariable Long id) {
        boolean flag = sysRoleService.deleteSysRole(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取系统角色详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysRole/{id}")
    @Operation(summary = "系统角色详情")
    @Permission("sys:role:info")
    public ApiResult<SysRoleVo> getSysRole(@PathVariable Long id) {
        SysRoleVo sysRoleVo = sysRoleService.getSysRoleById(id);
        return ApiResult.success(sysRoleVo);
    }

    /**
     * 系统角色分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysRolePage")
    @Operation(summary = "系统角色分页列表")
    @Permission("sys:role:page")
    public ApiResult<SysRoleVo> getSysRolePage(@Valid @RequestBody SysRoleQuery query) {
        Paging<SysRoleVo> paging = sysRoleService.getSysRolePage(query);
        return ApiResult.success(paging);
    }

    /**
     * 系统所有角色列表
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysRoleAllList")
    @Operation(summary = "系统所有角色列表")
    public ApiResult<SysRole> getSysRoleAllList() {
        List<SysRole> list = sysRoleService.getSysRoleAllList();
        return ApiResult.success(list);
    }

    /**
     * 设置角色权限
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/setRoleMenus")
    @Operation(summary = "设置角色权限")
    @Permission("sys:role:set-role-menus")
    public ApiResult setRoleMenus(@Valid @RequestBody RoleMenusDto roleMenusDto) {
        boolean flag = sysRoleService.setRoleMenus(roleMenusDto);
        return ApiResult.success(flag);
    }

    /**
     * 获取角色权限ID集合
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/getMenuIdsByRoleId/{roleId}")
    @Operation(summary = "获取角色权限ID集合")
    public ApiResult getMenuIdsByRoleId(@PathVariable Long roleId) {
        List<Long> list = sysMenuService.getMenuIdsByRoleId(roleId);
        return ApiResult.success(list);
    }

}
