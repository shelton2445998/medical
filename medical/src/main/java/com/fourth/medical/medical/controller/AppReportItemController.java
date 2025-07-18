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
 * 功能说明：
 * 本控制器为移动端用户提供体检报告检查项的查询服务，支持通过ID获取检查项详情、分页获取检查项列表、
 * 以及获取当前用户的检查项信息。主要用于App端体检报告详情展示、检查结果查看等场景。
 * 
 * 主要功能：
 * 1. 获取检查项详情 - 通过检查项ID查询详细信息
 * 2. 分页获取检查项列表 - 支持多条件分页查询
 * 3. 获取当前用户的检查项信息 - 查询用户的所有检查项
 * 4. 获取当前用户的检查项分页列表 - 分页查询用户的检查项
 * 
 * 业务流程：
 * 1. 用户在App端发起检查项查询请求
 * 2. 控制器接收请求参数，调用服务层进行业务处理
 * 3. 服务层返回检查项信息，控制器封装为统一响应格式
 * 4. 返回结果给前端展示
 * 
 * 权限控制：
 * - 仅限已登录用户访问（通过Token校验）
 * - 查询操作无需特殊权限
 * 
 * 数据安全：
 * - 仅返回当前用户的检查项信息，防止越权访问
 * - 日志记录所有查询操作，便于审计
 * 
 * 扩展点：
 * - 可扩展检查项标签、医生建议、异常标识等功能
 * - 支持多条件复合筛选
 * 
 * @author fourth
 * @since 2025-07-09
 * @version 1.0
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
     * 功能说明：
     * 根据检查项ID查询详细信息，返回检查项的基本资料、检查结果、医生建议等。
     * 主要用于App端体检报告详情页展示。
     * 
     * @param id 检查项ID
     * @return ApiResult<AppReportItemVo> 检查项详情信息
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
     * 功能说明：
     * 支持多条件分页查询检查项信息，便于用户浏览和筛选检查结果。
     * 
     * @param query 查询条件
     * @return ApiResult<AppReportItemVo> 分页检查项列表
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
     * 功能说明：
     * 查询当前用户的所有体检报告检查项信息，便于用户查看自己的检查结果。
     * 
     * @return ApiResult<List<ReportItemVo>> 当前用户的检查项列表
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
     * 功能说明：
     * 分页查询当前用户的体检报告检查项信息，便于用户浏览和筛选检查结果。
     * 
     * @param query 查询条件
     * @return ApiResult<Paging<AppReportItemVo>> 当前用户的检查项分页列表
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
