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
 * 系统配置 控制器
 *
 * @author geekidea
 * @since 2023-11-27
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
