package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppOverallResultQuery;
import com.fourth.medical.medical.service.OverallResultService;
import com.fourth.medical.medical.vo.AppOverallResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App总检结论 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App总检结论")
@RequestMapping("/app/overallResult")
public class AppOverallResultController {

    @Autowired
    private OverallResultService overallResultService;

    /**
     * 获取App总检结论详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App总检结论详情")
    @PostMapping("/getAppOverallResult/{id}")
    public ApiResult<AppOverallResultVo> getAppOverallResult(@PathVariable Long id) {
        log.info("获取App总检结论详情：{}", id);
        AppOverallResultVo appOverallResultVo = overallResultService.getAppOverallResultById(id);
        return ApiResult.success(appOverallResultVo);
    }

    /**
     * 获取App总检结论分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App总检结论分页列表")
    @PostMapping("/getAppOverallResultPage")
    public ApiResult<AppOverallResultVo> getAppOverallResultPage(@Valid @RequestBody AppOverallResultQuery query) {
        log.info("获取App总检结论分页列表：{}", query);
        Paging<AppOverallResultVo> paging = overallResultService.getAppOverallResultPage(query);
        return ApiResult.success(paging);
    }

}
