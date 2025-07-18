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
 * 系统文件管理控制器
 * 
 * 功能说明：
 * 本控制器负责管理系统中的文件资源，包括文件的上传、下载、存储管理、
 * 文件信息维护等功能。文件管理是系统的基础服务，为其他模块提供文件
 * 存储和访问能力，支持多种文件类型和存储方式。
 * 
 * 主要功能：
 * 1. 文件基础管理 - 文件的上传、下载、删除、查询
 * 2. 文件存储管理 - 文件的存储位置、访问路径管理
 * 3. 文件信息维护 - 文件的元数据信息管理
 * 4. 文件安全管理 - 文件的访问权限和安全控制
 * 5. 文件统计分析 - 文件使用情况统计和分析
 * 
 * 业务特点：
 * - 支持多种文件类型（图片、文档、视频等）
 * - 灵活的存储策略（本地存储、云存储等）
 * - 完整的文件生命周期管理
 * - 严格的安全控制和访问权限管理
 * 
 * @author geekidea
 * @since 2023-11-26
 * @version 1.0
 */
@Slf4j  // 自动生成日志对象
@RestController  // REST控制器注解
@Tag(name = "系统文件")  // Swagger文档标签
@RequestMapping("/admin/sysFile")  // 请求路径映射
public class SysFileController {

    /**
     * 系统文件服务接口
     * 提供文件管理的业务逻辑处理功能
     */
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
