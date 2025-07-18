package com.fourth.medical.system.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.system.dto.SysConfigDto;
import com.fourth.medical.system.query.SysConfigQuery;
import com.fourth.medical.system.service.SysConfigService;
import com.fourth.medical.system.vo.SysConfigVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 系统配置管理控制器
 * 
 * 功能说明：
 * 本控制器负责管理系统的各种配置参数，包括系统参数设置、业务配置管理、
 * 功能开关控制等。通过配置管理，可以灵活调整系统行为，无需重启系统
 * 即可生效，提高了系统的可维护性和灵活性。
 * 
 * 主要功能：
 * 1. 系统参数管理 - 系统级别的配置参数设置
 * 2. 业务配置管理 - 业务模块的配置参数管理
 * 3. 功能开关控制 - 系统功能的启用/禁用控制
 * 4. 配置分类管理 - 按类别组织配置参数
 * 5. 配置历史管理 - 配置变更历史记录和回滚
 * 
 * 业务特点：
 * - 动态配置，无需重启系统即可生效
 * - 分类管理，便于配置的组织和维护
 * - 权限控制，确保配置的安全性
 * - 历史记录，支持配置的版本管理和回滚
 * 
 * @author geekidea
 * @since 2023-11-27
 * @version 1.0
 */
@Slf4j
@RestController
@Tag(name = "系统配置")
@RequestMapping("/admin/sysConfig")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 添加系统配置
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(value = "添加系统配置", type = SysLogType.ADD)
    @PostMapping("/addSysConfig")
    @Operation(summary = "添加系统配置")
    @Permission("sys:config:add")
    public ApiResult addSysConfig(@Valid @RequestBody SysConfigDto dto) {
        boolean flag = sysConfigService.addSysConfig(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改系统配置
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(value = "修改系统配置", type = SysLogType.UPDATE)
    @PostMapping("/updateSysConfig")
    @Operation(summary = "修改系统配置")
    @Permission("sys:config:update")
    public ApiResult updateSysConfig(@Valid @RequestBody SysConfigDto dto) {
        boolean flag = sysConfigService.updateSysConfig(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除系统配置
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(value = "删除系统配置", type = SysLogType.DELETE)
    @PostMapping("/deleteSysConfig/{id}")
    @Operation(summary = "删除系统配置")
    @Permission("sys:config:delete")
    public ApiResult deleteSysConfig(@PathVariable Long id) {
        boolean flag = sysConfigService.deleteSysConfig(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取系统配置详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysConfig/{id}")
    @Operation(summary = "系统配置详情")
    @Permission("sys:config:info")
    public ApiResult<SysConfigVo> getSysConfig(@PathVariable Long id) {
        SysConfigVo sysConfigVo = sysConfigService.getSysConfigById(id);
        return ApiResult.success(sysConfigVo);
    }

    /**
     * 系统配置分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysConfigPage")
    @Operation(summary = "系统配置分页列表")
    @Permission("sys:config:page")
    public ApiResult<SysConfigVo> getSysConfigPage(@Valid @RequestBody SysConfigQuery query) {
        Paging<SysConfigVo> paging = sysConfigService.getSysConfigPage(query);
        return ApiResult.success(paging);
    }

}
