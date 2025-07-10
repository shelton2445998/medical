package com.fourth.medical.system.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.system.dto.SysMenuDto;
import com.fourth.medical.system.query.SysMenuQuery;
import com.fourth.medical.system.service.SysMenuService;
import com.fourth.medical.system.vo.SysMenuTreeVo;
import com.fourth.medical.system.vo.SysMenuVo;
import com.fourth.medical.system.vo.SysNavMenuTreeVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统菜单 控制器
 *
 * @author geekidea
 * @since 2022-12-26
 */
@Slf4j
@RestController
@Tag(name = "系统菜单")
@RequestMapping("/admin/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 添加系统菜单
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @PostMapping("/addSysMenu")
    @Operation(summary = "添加系统菜单")
    @Permission("sys:menu:add")
    public ApiResult addSysMenu(@Valid @RequestBody SysMenuDto dto) {
        boolean flag = sysMenuService.addSysMenu(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改系统菜单
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @PostMapping("/updateSysMenu")
    @Operation(summary = "修改系统菜单")
    @Permission("sys:menu:update")
    public ApiResult updateSysMenu(@Valid @RequestBody SysMenuDto dto) {
        boolean flag = sysMenuService.updateSysMenu(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除系统菜单
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/deleteSysMenu/{id}")
    @Operation(summary = "删除系统菜单")
    @Permission("sys:menu:delete")
    public ApiResult deleteSysMenu(@PathVariable Long id) {
        boolean flag = sysMenuService.deleteSysMenu(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取系统菜单详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysMenu/{id}")
    @Operation(summary = "系统菜单详情")
    @Permission("sys:menu:info")
    public ApiResult<SysMenuVo> getSysMenu(@PathVariable Long id) {
        SysMenuVo sysMenuVo = sysMenuService.getSysMenuById(id);
        return ApiResult.success(sysMenuVo);
    }

    /**
     * 获取所有的系统菜单树形列表
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/getAllSysMenuTreeList")
    @Operation(summary = "获取所有的系统菜单树形列表")
    @Permission("sys:menu:all-tree-list")
    public ApiResult<SysMenuTreeVo> getAllSysMenuTreeList(@Valid @RequestBody SysMenuQuery query) {
        List<SysMenuTreeVo> list = sysMenuService.getAllSysMenuTreeList(query);
        return ApiResult.success(list);
    }

    /**
     * 获取启用的系统菜单树形列表
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysMenuTreeList")
    @Operation(summary = "获取启用的系统菜单树形列表")
    @Permission("sys:menu:tree-list")
    public ApiResult<SysMenuTreeVo> getSysMenuTreeList() {
        List<SysMenuTreeVo> list = sysMenuService.getSysMenuTreeList();
        return ApiResult.success(list);
    }

    /**
     * 获取当前用户的导航菜单
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/getNavMenuTreeList")
    @Operation(summary = "获取当前用户的导航菜单")
    public ApiResult<SysNavMenuTreeVo> getNavMenuTreeList() {
        List<SysNavMenuTreeVo> list = sysMenuService.getNavMenuTreeList();
        return ApiResult.success(list);
    }

}
