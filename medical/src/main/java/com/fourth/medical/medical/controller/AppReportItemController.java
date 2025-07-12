package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.util.AppLoginUtil;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppReportItemQuery;
import com.fourth.medical.medical.service.ReportItemService;
import com.fourth.medical.medical.vo.AppReportItemVo;
import com.fourth.medical.medical.vo.ReportItemVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * App体检报告检查项信息 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App体检报告检查项信息")
@RequestMapping("/app/reportItem")
public class AppReportItemController {

    @Autowired
    private ReportItemService reportItemService;

    /**
     * 获取App体检报告检查项信息详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App体检报告检查项信息详情")
    @PostMapping("/getAppReportItem/{id}")
    public ApiResult<AppReportItemVo> getAppReportItem(@PathVariable Long id) {
        log.info("获取App体检报告检查项信息详情：{}", id);
        AppReportItemVo appReportItemVo = reportItemService.getAppReportItemById(id);
        return ApiResult.success(appReportItemVo);
    }

    /**
     * 获取App体检报告检查项信息分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App体检报告检查项信息分页列表")
    @PostMapping("/getAppReportItemPage")
    public ApiResult<AppReportItemVo> getAppReportItemPage(@Valid @RequestBody AppReportItemQuery query) {
        log.info("获取App体检报告检查项信息分页列表：{}", query);
        Paging<AppReportItemVo> paging = reportItemService.getAppReportItemPage(query);
        return ApiResult.success(paging);
    }
    
    /**
     * 获取当前用户的体检报告检查项信息列表
     *
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取当前用户的体检报告检查项信息列表")
    @GetMapping("/getCurrentUserReportItems")
    public ApiResult<List<ReportItemVo>> getCurrentUserReportItems() {
        Long userId = AppLoginUtil.getUserId();
        log.info("获取当前用户的体检报告检查项信息列表，用户ID：{}", userId);
        List<ReportItemVo> reportItems = reportItemService.getReportItemsByUserId(userId);
        return ApiResult.success(reportItems);
    }
    
    /**
     * 获取当前用户的体检报告检查项信息分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取当前用户的体检报告检查项信息分页列表")
    @PostMapping("/getCurrentUserReportItemPage")
    public ApiResult<Paging<AppReportItemVo>> getCurrentUserReportItemPage(@Valid @RequestBody AppReportItemQuery query) {
        Long userId = AppLoginUtil.getUserId();
        log.info("获取当前用户的体检报告检查项信息分页列表，用户ID：{}", userId);
        // 设置查询条件中的用户ID
        query.setUserId(userId);
        Paging<AppReportItemVo> paging = reportItemService.getAppReportItemPage(query);
        return ApiResult.success(paging);
    }
}
