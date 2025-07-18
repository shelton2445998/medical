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
 * 功能说明：
 * 本控制器为移动端用户提供体检报告总览的查询服务，支持通过ID获取报告详情、分页获取报告列表。
 * 主要用于App端用户查看自己的体检报告、历史记录等场景。
 * 
 * 主要功能：
 * 1. 获取体检报告详情 - 通过报告ID查询详细信息
 * 2. 分页获取体检报告列表 - 支持多条件分页查询
 * 
 * 业务流程：
 * 1. 用户在App端发起体检报告查询请求
 * 2. 控制器接收请求参数，调用服务层进行业务处理
 * 3. 服务层返回报告信息，控制器封装为统一响应格式
 * 4. 返回结果给前端展示
 * 
 * 权限控制：
 * - 仅限已登录用户访问（通过Token校验）
 * - 查询操作无需特殊权限
 * 
 * 数据安全：
 * - 仅返回当前用户的体检报告信息，防止越权访问
 * - 日志记录所有查询操作，便于审计
 * 
 * 扩展点：
 * - 可扩展报告标签、医生建议、报告下载等功能
 * - 支持多条件复合筛选
 * 
 * @author fourth
 * @since 2025-07-14
 * @version 1.0
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
     * 功能说明：
     * 根据报告ID查询体检报告的详细信息，返回报告的基本资料、检查结果、医生建议等。
     * 主要用于App端体检报告详情页展示。
     * 
     * @param id 报告ID
     * @param request HTTP请求对象，用于获取用户Token
     * @return ApiResult<AppReportVo> 体检报告详情信息
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
     * 功能说明：
     * 支持多条件分页查询体检报告信息，便于用户浏览和筛选历史报告。
     * 
     * @param query 查询条件
     * @param request HTTP请求对象，用于获取用户Token
     * @return ApiResult<AppReportVo> 分页体检报告列表
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

    /**
     * 删除App体检报告
     * 
     * 功能说明：
     * 删除用户指定的体检报告。出于数据安全考虑，采用逻辑删除方式，
     * 即将报告状态标记为已删除，而不是物理删除数据记录。
     * 
     * 业务流程：
     * 1. 接收要删除的报告ID
     * 2. 验证报告ID的有效性
     * 3. 检查报告是否存在
     * 4. 验证用户权限（只能删除自己的报告）
     * 5. 验证是否可以删除（业务规则检查）
     * 6. 执行删除操作（逻辑删除）
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * 数据验证：
     * - 报告ID必须存在
     * - 报告必须存在且未被删除
     * - 报告必须属于当前用户
     * - 报告状态必须允许删除
     * 
     * 业务规则：
     * - 已发布的报告不能删除
     * - 有关联预约的报告需要特殊处理
     * - 只能删除自己的报告
     * 
     * @param id 报告ID
     * @param request HTTP请求对象，用于获取用户Token
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     */
    @Operation(summary = "删除App体检报告")
    @DeleteMapping("/deleteAppReport/{id}")
    public ApiResult deleteAppReport(@PathVariable Long id, HttpServletRequest request) {
        log.info("删除App体检报告：{}", id);
        // 从请求中获取token
        String token = TokenUtil.getToken(request);
        log.info("获取到token: {}", token);
        
        boolean flag = reportService.deleteAppReport(id, token);
        return ApiResult.result(flag);
    }

}
