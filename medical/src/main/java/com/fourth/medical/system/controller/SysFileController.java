package com.fourth.medical.system.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.system.dto.SysFileDto;
import com.fourth.medical.system.query.SysFileQuery;
import com.fourth.medical.system.service.SysFileService;
import com.fourth.medical.system.vo.SysFileVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 系统文件 控制器
 *
 * @author geekidea
 * @since 2023-11-26
 */
@Slf4j
@RestController
@Tag(name = "系统文件")
@RequestMapping("/admin/sysFile")
public class SysFileController {

    @Autowired
    private SysFileService sysFileService;

    /**
     * 修改系统文件
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(value = "修改系统文件", type = SysLogType.UPDATE)
    @PostMapping("/updateSysFile")
    @Operation(summary = "修改系统文件")
    @Permission("sys:file:update")
    public ApiResult updateSysFile(@Valid @RequestBody SysFileDto dto) {
        boolean flag = sysFileService.updateSysFile(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除系统文件
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(value = "删除系统文件", type = SysLogType.DELETE)
    @PostMapping("/deleteSysFile/{id}")
    @Operation(summary = "删除系统文件")
    @Permission("sys:file:delete")
    public ApiResult deleteSysFile(@PathVariable Long id) {
        boolean flag = sysFileService.deleteSysFile(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取系统文件详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysFile/{id}")
    @Operation(summary = "系统文件详情")
    @Permission("sys:file:info")
    public ApiResult<SysFileVo> getSysFile(@PathVariable Long id) {
        SysFileVo sysFileVo = sysFileService.getSysFileById(id);
        return ApiResult.success(sysFileVo);
    }

    /**
     * 系统文件分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysFilePage")
    @Operation(summary = "系统文件分页列表")
    @Permission("sys:file:page")
    public ApiResult<SysFileVo> getSysFilePage(@Valid @RequestBody SysFileQuery query) {
        Paging<SysFileVo> paging = sysFileService.getSysFilePage(query);
        return ApiResult.success(paging);
    }

}
