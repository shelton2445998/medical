package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.util.LoginUtil;
import com.fourth.medical.auth.util.TokenUtil;
import com.fourth.medical.auth.vo.LoginVo;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.ReportItemQuery;
import com.fourth.medical.medical.query.ReportItemDetailQuery;
import com.fourth.medical.medical.service.ReportItemService;
import com.fourth.medical.medical.service.ReportItemDetailService;
import com.fourth.medical.medical.vo.ReportItemVo;
import com.fourth.medical.medical.vo.ReportItemDetailVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 医生端 - 体检报告管理 控制器
 *
 * @author fourth
 * @since 2025-07-12
 */
@Slf4j
@RestController
@Tag(name = "医生端 - 体检报告管理")
@RequestMapping("/doctor/report")
public class DoctorReportController {

    @Autowired
    private ReportItemService reportItemService;
    
    @Autowired
    private ReportItemDetailService reportItemDetailService;

    /**
     * 获取医生负责的体检报告列表
     *
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param patientKeyword 患者关键词（姓名、手机号）
     * @param recordType 记录类型（可选）
     * @param request HTTP请求对象，用于获取请求头中的token
     * @return 体检报告列表
     */
    @Operation(summary = "获取医生负责的体检报告列表")
    @GetMapping("/list")
    public ApiResult<ReportItemVo> getReportList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String patientKeyword,
            @RequestParam(required = false) String recordType,
            HttpServletRequest request) {
        
        // 从请求头中获取token
        String token = TokenUtil.getToken(request);
        if (StringUtils.isBlank(token)) {
            return ApiResult.fail("未登录或登录已过期");
        }
        
        // 通过token获取医生信息
        LoginVo loginVo = LoginUtil.getLoginVo(token);
        if (loginVo == null) {
            return ApiResult.fail("无法获取医生信息，请重新登录");
        }
        
        Long doctorId = loginVo.getUserId();
        log.info("获取医生体检报告列表，doctorId: {}, pageNum: {}, pageSize: {}, patientKeyword: {}, recordType: {}", 
                doctorId, pageNum, pageSize, patientKeyword, recordType);
        
        // 构建查询条件
        ReportItemQuery query = new ReportItemQuery();
        query.setPageIndex(pageNum);
        query.setPageSize(pageSize);
        query.setDoctorId(doctorId);
        query.setPatientKeyword(patientKeyword);
        
        // 调用服务查询报告列表
        Paging<ReportItemVo> paging = reportItemService.getReportItemPage(query);
        return ApiResult.success(paging);
    }

    /**
     * 获取体检报告详情
     *
     * @param reportId 报告ID
     * @param request HTTP请求对象，用于获取请求头中的token
     * @return 体检报告详情
     */
    @Operation(summary = "获取体检报告详情")
    @GetMapping("/detail/{reportId}")
    public ApiResult<ReportItemVo> getReportDetail(
            @PathVariable("reportId") Long reportId,
            HttpServletRequest request) {
        
        // 从请求头中获取token
        String token = TokenUtil.getToken(request);
        if (StringUtils.isBlank(token)) {
            return ApiResult.fail("未登录或登录已过期");
        }
        
        // 通过token获取医生信息
        LoginVo loginVo = LoginUtil.getLoginVo(token);
        if (loginVo == null) {
            return ApiResult.fail("无法获取医生信息，请重新登录");
        }
        
        Long doctorId = loginVo.getUserId();
        log.info("获取体检报告详情，doctorId: {}, reportId: {}", doctorId, reportId);
        
        // 获取报告基本信息
        ReportItemVo reportItemVo = reportItemService.getReportItemById(reportId);
        
        // 检查权限，确保只能查看自己负责的报告
        if (reportItemVo != null && !doctorId.equals(reportItemVo.getDoctorId())) {
            return ApiResult.fail("无权查看此报告");
        }
        
        return ApiResult.success(reportItemVo);
    }

    /**
     * 获取体检报告检查项明细
     *
     * @param reportId 报告ID
     * @param request HTTP请求对象，用于获取请求头中的token
     * @return 体检报告检查项明细列表
     */
    @Operation(summary = "获取体检报告检查项明细")
    @GetMapping("/detail/items/{reportId}")
    public ApiResult<ReportItemDetailVo> getReportItemDetails(
            @PathVariable("reportId") Long reportId,
            HttpServletRequest request) {
        
        // 从请求头中获取token
        String token = TokenUtil.getToken(request);
        if (StringUtils.isBlank(token)) {
            return ApiResult.fail("未登录或登录已过期");
        }
        
        // 通过token获取医生信息
        LoginVo loginVo = LoginUtil.getLoginVo(token);
        if (loginVo == null) {
            return ApiResult.fail("无法获取医生信息，请重新登录");
        }
        
        Long doctorId = loginVo.getUserId();
        log.info("获取体检报告检查项明细，doctorId: {}, reportId: {}", doctorId, reportId);
        
        // 检查报告权限
        ReportItemVo reportItemVo = reportItemService.getReportItemById(reportId);
        if (reportItemVo == null || !doctorId.equals(reportItemVo.getDoctorId())) {
            return ApiResult.fail("无权查看此报告");
        }
        
        // 构建查询条件
        ReportItemDetailQuery query = new ReportItemDetailQuery();
        query.setReportId(reportId);
        
        // 查询报告明细列表
        Paging<ReportItemDetailVo> paging = reportItemDetailService.getReportItemDetailPage(query);
        return ApiResult.success(paging);
    }
} 