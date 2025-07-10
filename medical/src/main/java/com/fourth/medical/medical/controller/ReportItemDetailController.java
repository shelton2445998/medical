package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.ReportItemDetailDto;
import com.fourth.medical.medical.query.ReportItemDetailQuery;
import com.fourth.medical.medical.service.ReportItemDetailService;
import com.fourth.medical.medical.vo.ReportItemDetailVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 体检报告检查项明细 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "体检报告检查项明细")
@RequestMapping("/admin/reportItemDetail")
public class ReportItemDetailController {

    @Autowired
    private ReportItemDetailService reportItemDetailService;

    /**
     * 添加体检报告检查项明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加体检报告检查项明细")
    @PostMapping("/addReportItemDetail")
    @Permission("report:item:detail:add")
    public ApiResult addReportItemDetail(@Valid @RequestBody ReportItemDetailDto dto) {
        log.info("添加体检报告检查项明细：{}", dto);
        boolean flag = reportItemDetailService.addReportItemDetail(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改体检报告检查项明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改体检报告检查项明细")
    @PostMapping("/updateReportItemDetail")
    @Permission("report:item:detail:update")
    public ApiResult updateReportItemDetail(@Valid @RequestBody ReportItemDetailDto dto) {
        log.info("修改体检报告检查项明细：{}", dto);
        boolean flag = reportItemDetailService.updateReportItemDetail(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除体检报告检查项明细
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除体检报告检查项明细")
    @PostMapping("/deleteReportItemDetail/{id}")
    @Permission("report:item:detail:delete")
    public ApiResult deleteReportItemDetail(@PathVariable Long id) {
        log.info("删除体检报告检查项明细：{}", id);
        boolean flag = reportItemDetailService.deleteReportItemDetail(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取体检报告检查项明细详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检报告检查项明细详情")
    @PostMapping("/getReportItemDetail/{id}")
    @Permission("report:item:detail:info")
    public ApiResult<ReportItemDetailVo> getReportItemDetail(@PathVariable Long id) {
        log.info("获取体检报告检查项明细详情：{}", id);
        ReportItemDetailVo reportItemDetailVo = reportItemDetailService.getReportItemDetailById(id);
        return ApiResult.success(reportItemDetailVo);
    }

    /**
     * 获取体检报告检查项明细分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检报告检查项明细分页列表")
    @PostMapping("/getReportItemDetailPage")
    @Permission("report:item:detail:page")
    public ApiResult<ReportItemDetailVo> getReportItemDetailPage(@Valid @RequestBody ReportItemDetailQuery query) {
        log.info("获取体检报告检查项明细分页列表：{}", query);
        Paging<ReportItemDetailVo> paging = reportItemDetailService.getReportItemDetailPage(query);
        return ApiResult.success(paging);
    }

}
