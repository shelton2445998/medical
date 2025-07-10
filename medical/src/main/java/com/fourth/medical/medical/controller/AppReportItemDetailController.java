package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppReportItemDetailQuery;
import com.fourth.medical.medical.service.ReportItemDetailService;
import com.fourth.medical.medical.vo.AppReportItemDetailVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App体检报告检查项明细 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App体检报告检查项明细")
@RequestMapping("/app/reportItemDetail")
public class AppReportItemDetailController {

    @Autowired
    private ReportItemDetailService reportItemDetailService;

    /**
     * 获取App体检报告检查项明细详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App体检报告检查项明细详情")
    @PostMapping("/getAppReportItemDetail/{id}")
    public ApiResult<AppReportItemDetailVo> getAppReportItemDetail(@PathVariable Long id) {
        log.info("获取App体检报告检查项明细详情：{}", id);
        AppReportItemDetailVo appReportItemDetailVo = reportItemDetailService.getAppReportItemDetailById(id);
        return ApiResult.success(appReportItemDetailVo);
    }

    /**
     * 获取App体检报告检查项明细分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App体检报告检查项明细分页列表")
    @PostMapping("/getAppReportItemDetailPage")
    public ApiResult<AppReportItemDetailVo> getAppReportItemDetailPage(@Valid @RequestBody AppReportItemDetailQuery query) {
        log.info("获取App体检报告检查项明细分页列表：{}", query);
        Paging<AppReportItemDetailVo> paging = reportItemDetailService.getAppReportItemDetailPage(query);
        return ApiResult.success(paging);
    }

}
