package com.fourth.medical.system.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.system.query.SysLogQuery;
import com.fourth.medical.system.service.SysLogService;
import com.fourth.medical.system.vo.SysLogVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 系统日志 控制器
 *
 * @author geekidea
 * @since 2023-02-16
 */
@Slf4j
@RestController
@Tag(name = "系统日志")
@RequestMapping("/admin/sysLog")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 获取系统日志详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysLog/{id}")
    @Operation(summary = "系统日志详情")
    @Permission("sys:log:info")
    public ApiResult<SysLogVo> getSysLog(@PathVariable Long id) {
        SysLogVo sysLogVo = sysLogService.getSysLogById(id);
        return ApiResult.success(sysLogVo);
    }

    /**
     * 系统日志分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysLogPage")
    @Operation(summary = "系统日志分页列表")
    @Permission("sys:log:page")
    public ApiResult<SysLogVo> getSysLogPage(@Valid @RequestBody SysLogQuery query) {
        Paging<SysLogVo> paging = sysLogService.getSysLogPage(query);
        return ApiResult.success(paging);
    }

}
