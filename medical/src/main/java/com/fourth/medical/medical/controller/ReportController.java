package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.ReportDto;
import com.fourth.medical.medical.query.ReportQuery;
import com.fourth.medical.medical.service.ReportService;
import com.fourth.medical.medical.vo.ReportVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 体检报告总 控制器
 *
 * @author fourth
 * @since 2025-07-14
 */
@Slf4j
@RestController
@Tag(name = "体检报告总")
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 添加体检报告总
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加体检报告总")
    @PostMapping("/addReport")
    @Permission("report:add")
    public ApiResult addReport(@Valid @RequestBody ReportDto dto) {
        log.info("添加体检报告总：{}", dto);
        boolean flag = reportService.addReport(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改体检报告总
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改体检报告总")
    @PostMapping("/updateReport")
    @Permission("report:update")
    public ApiResult updateReport(@Valid @RequestBody ReportDto dto) {
        log.info("修改体检报告总：{}", dto);
        boolean flag = reportService.updateReport(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除体检报告总
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除体检报告总")
    @PostMapping("/deleteReport/{id}")
    @Permission("report:delete")
    public ApiResult deleteReport(@PathVariable Long id) {
        log.info("删除体检报告总：{}", id);
        boolean flag = reportService.deleteReport(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取体检报告总详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检报告总详情")
    @PostMapping("/getReport/{id}")
    @Permission("report:info")
    public ApiResult<ReportVo> getReport(@PathVariable Long id) {
        log.info("获取体检报告总详情：{}", id);
        ReportVo reportVo = reportService.getReportById(id);
        return ApiResult.success(reportVo);
    }

    /**
     * 获取体检报告总分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检报告总分页列表")
    @PostMapping("/getReportPage")
    @Permission("report:page")
    public ApiResult<ReportVo> getReportPage(@Valid @RequestBody ReportQuery query) {
        log.info("获取体检报告总分页列表：{}", query);
        Paging<ReportVo> paging = reportService.getReportPage(query);
        return ApiResult.success(paging);
    }

}
