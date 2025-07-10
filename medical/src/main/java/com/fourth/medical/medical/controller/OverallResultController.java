package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.OverallResultDto;
import com.fourth.medical.medical.query.OverallResultQuery;
import com.fourth.medical.medical.service.OverallResultService;
import com.fourth.medical.medical.vo.OverallResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 总检结论 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "总检结论")
@RequestMapping("/admin/overallResult")
public class OverallResultController {

    @Autowired
    private OverallResultService overallResultService;

    /**
     * 添加总检结论
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加总检结论")
    @PostMapping("/addOverallResult")
    @Permission("overall:result:add")
    public ApiResult addOverallResult(@Valid @RequestBody OverallResultDto dto) {
        log.info("添加总检结论：{}", dto);
        boolean flag = overallResultService.addOverallResult(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改总检结论
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改总检结论")
    @PostMapping("/updateOverallResult")
    @Permission("overall:result:update")
    public ApiResult updateOverallResult(@Valid @RequestBody OverallResultDto dto) {
        log.info("修改总检结论：{}", dto);
        boolean flag = overallResultService.updateOverallResult(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除总检结论
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除总检结论")
    @PostMapping("/deleteOverallResult/{id}")
    @Permission("overall:result:delete")
    public ApiResult deleteOverallResult(@PathVariable Long id) {
        log.info("删除总检结论：{}", id);
        boolean flag = overallResultService.deleteOverallResult(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取总检结论详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取总检结论详情")
    @PostMapping("/getOverallResult/{id}")
    @Permission("overall:result:info")
    public ApiResult<OverallResultVo> getOverallResult(@PathVariable Long id) {
        log.info("获取总检结论详情：{}", id);
        OverallResultVo overallResultVo = overallResultService.getOverallResultById(id);
        return ApiResult.success(overallResultVo);
    }

    /**
     * 获取总检结论分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取总检结论分页列表")
    @PostMapping("/getOverallResultPage")
    @Permission("overall:result:page")
    public ApiResult<OverallResultVo> getOverallResultPage(@Valid @RequestBody OverallResultQuery query) {
        log.info("获取总检结论分页列表：{}", query);
        Paging<OverallResultVo> paging = overallResultService.getOverallResultPage(query);
        return ApiResult.success(paging);
    }

}
