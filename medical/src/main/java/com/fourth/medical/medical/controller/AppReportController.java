package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppReportQuery;
import com.fourth.medical.medical.service.ReportService;
import com.fourth.medical.medical.vo.AppReportVo;
import com.fourth.medical.auth.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * App体检报告总 控制器
 *
 * @author fourth
 * @since 2025-07-14
 */
@Slf4j
@RestController
@Tag(name = "App体检报告总")
@RequestMapping("/app/report")
public class AppReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 获取App体检报告总详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App体检报告总详情")
    @PostMapping("/getAppReport/{id}")
    public ApiResult<AppReportVo> getAppReport(@PathVariable Long id, HttpServletRequest request) {
        log.info("获取App体检报告总详情：{}", id);
        // 从请求中获取token
        String token = TokenUtil.getToken(request);
        log.info("获取到token: {}", token);
        
        AppReportVo appReportVo = reportService.getAppReportById(id, token);
        return ApiResult.success(appReportVo);
    }

    /**
     * 获取App体检报告总分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App体检报告总分页列表")
    @PostMapping("/getAppReportPage")
    public ApiResult<AppReportVo> getAppReportPage(@Valid @RequestBody AppReportQuery query, HttpServletRequest request) {
        log.info("获取App体检报告总分页列表：{}", query);
        // 从请求中获取token
        String token = TokenUtil.getToken(request);
        log.info("获取到token: {}", token);
        
        Paging<AppReportVo> paging = reportService.getAppReportPage(query, token);
        return ApiResult.success(paging);
    }

}
