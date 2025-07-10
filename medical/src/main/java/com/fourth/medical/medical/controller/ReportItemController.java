package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.ReportItemDto;
import com.fourth.medical.medical.query.ReportItemQuery;
import com.fourth.medical.medical.service.ReportItemService;
import com.fourth.medical.medical.vo.ReportItemVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 体检报告检查项信息 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "体检报告检查项信息")
@RequestMapping("/admin/reportItem")
public class ReportItemController {

    @Autowired
    private ReportItemService reportItemService;

    /**
     * 添加体检报告检查项信息
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加体检报告检查项信息")
    @PostMapping("/addReportItem")
    @Permission("report:item:add")
    public ApiResult addReportItem(@Valid @RequestBody ReportItemDto dto) {
        log.info("添加体检报告检查项信息：{}", dto);
        boolean flag = reportItemService.addReportItem(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改体检报告检查项信息
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改体检报告检查项信息")
    @PostMapping("/updateReportItem")
    @Permission("report:item:update")
    public ApiResult updateReportItem(@Valid @RequestBody ReportItemDto dto) {
        log.info("修改体检报告检查项信息：{}", dto);
        boolean flag = reportItemService.updateReportItem(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除体检报告检查项信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除体检报告检查项信息")
    @PostMapping("/deleteReportItem/{id}")
    @Permission("report:item:delete")
    public ApiResult deleteReportItem(@PathVariable Long id) {
        log.info("删除体检报告检查项信息：{}", id);
        boolean flag = reportItemService.deleteReportItem(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取体检报告检查项信息详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检报告检查项信息详情")
    @PostMapping("/getReportItem/{id}")
    @Permission("report:item:info")
    public ApiResult<ReportItemVo> getReportItem(@PathVariable Long id) {
        log.info("获取体检报告检查项信息详情：{}", id);
        ReportItemVo reportItemVo = reportItemService.getReportItemById(id);
        return ApiResult.success(reportItemVo);
    }

    /**
     * 获取体检报告检查项信息分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检报告检查项信息分页列表")
    @PostMapping("/getReportItemPage")
    @Permission("report:item:page")
    public ApiResult<ReportItemVo> getReportItemPage(@Valid @RequestBody ReportItemQuery query) {
        log.info("获取体检报告检查项信息分页列表：{}", query);
        Paging<ReportItemVo> paging = reportItemService.getReportItemPage(query);
        return ApiResult.success(paging);
    }

}
