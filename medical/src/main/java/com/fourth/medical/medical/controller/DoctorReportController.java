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
import com.fourth.medical.medical.entity.Doctor;
import com.fourth.medical.medical.service.DoctorService;
import com.fourth.medical.medical.dto.ReportItemDetailDto;
import com.fourth.medical.medical.dto.ReportItemDto;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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

    @Autowired
    private DoctorService doctorService;

    /**
     * 获取医生负责的体检报告列表
     *
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param patientKeyword 患者关键词（姓名、手机号）
     * @param request HTTP请求对象，用于获取请求头中的token
     * @return 体检报告列表
     */
    @Operation(summary = "获取医生负责的体检报告列表")
    @GetMapping("/list")
    public ApiResult<Paging<ReportItemVo>> getReportList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String patientKeyword,
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
        
        // 查询医生信息获取部门ID
        Doctor doctor = doctorService.getById(doctorId);
        if (doctor == null) {
            return ApiResult.fail("医生信息不存在");
        }
        Long departmentId = doctor.getDepartmentId();
        
        log.info("获取医生体检报告列表，doctorId: {}, departmentId: {}, pageNum: {}, pageSize: {}, patientKeyword: {}", 
                doctorId, departmentId, pageNum, pageSize, patientKeyword);
        
        // 构建查询条件
        ReportItemQuery query = new ReportItemQuery();
        query.setPageIndex(pageNum);
        query.setPageSize(pageSize);
        query.setDoctorId(doctorId);
        query.setDepartmentId(departmentId); // 添加部门ID作为查询条件
        query.setIncludeZeroDoctorId(true); // 设置包含doctorId为0的记录
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
        
        // 查询医生信息获取部门ID
        Doctor doctor = doctorService.getById(doctorId);
        if (doctor == null) {
            return ApiResult.fail("医生信息不存在");
        }
        Long departmentId = doctor.getDepartmentId();
        
        log.info("获取体检报告详情，doctorId: {}, departmentId: {}, reportId: {}", doctorId, departmentId, reportId);
        
        // 获取报告基本信息
        ReportItemVo reportItemVo = reportItemService.getReportItemById(reportId);
        
        if (reportItemVo == null) {
            return ApiResult.fail("报告不存在");
        }
        
        // 检查权限
        // 1. 如果是医生自己负责的报告，可以查看
        // 2. 如果报告的doctorId为0，且医生部门ID与报告所属医生部门ID相同，也可以查看
        boolean hasPermission = doctorId.equals(reportItemVo.getDoctorId()) || 
                               (reportItemVo.getDoctorId() != null && reportItemVo.getDoctorId() == 0);
        
        if (!hasPermission) {
            return ApiResult.fail("无权查看此报告");
        }
        
        // 如果医生不是报告的负责人，但有权限查看（即doctorId为0的情况），则更新报告的doctorId为当前医生
        if (reportItemVo.getDoctorId() != null && reportItemVo.getDoctorId() == 0) {
            reportItemService.updateReportItemDoctor(reportId, doctorId);
            // 重新获取更新后的报告信息
            reportItemVo = reportItemService.getReportItemById(reportId);
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
        
        // 查询医生信息获取部门ID
        Doctor doctor = doctorService.getById(doctorId);
        if (doctor == null) {
            return ApiResult.fail("医生信息不存在");
        }
        Long departmentId = doctor.getDepartmentId();
        
        log.info("获取体检报告检查项明细，doctorId: {}, departmentId: {}, reportId: {}", doctorId, departmentId, reportId);
        
        // 检查报告权限
        ReportItemVo reportItemVo = reportItemService.getReportItemById(reportId);
        if (reportItemVo == null) {
            return ApiResult.fail("报告不存在");
        }
        
        // 检查权限
        // 1. 如果是医生自己负责的报告，可以查看
        // 2. 如果报告的doctorId为0，且医生部门ID与报告所属医生部门ID相同，也可以查看
        boolean hasPermission = doctorId.equals(reportItemVo.getDoctorId()) || 
                               (reportItemVo.getDoctorId() != null && reportItemVo.getDoctorId() == 0);
        
        if (!hasPermission) {
            return ApiResult.fail("无权查看此报告");
        }
        
        // 如果医生不是报告的负责人，但有权限查看（即doctorId为0的情况），则更新报告的doctorId为当前医生
        if (reportItemVo.getDoctorId() != null && reportItemVo.getDoctorId() == 0) {
            reportItemService.updateReportItemDoctor(reportId, doctorId);
        }
        
        // 构建查询条件
        ReportItemDetailQuery query = new ReportItemDetailQuery();
        query.setReportId(reportId);
        
        // 查询报告明细列表
        Paging<ReportItemDetailVo> paging = reportItemDetailService.getReportItemDetailPage(query);
        return ApiResult.success(paging);
    }

    /**
     * 更新体检报告检查项明细
     *
     * @param reportId 报告ID
     * @param reportItemDetailDto 体检报告检查项明细数据
     * @param request HTTP请求对象，用于获取请求头中的token
     * @return 更新结果
     */
    @Operation(summary = "更新体检报告检查项明细")
    @PostMapping("/detail/items/update")
    public ApiResult<?> updateReportItemDetails(
            @RequestParam Long reportId,
            @RequestBody ReportItemDetailDto reportItemDetailDto,
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
        
        log.info("更新体检报告检查项明细，doctorId: {}, reportId: {}", doctorId, reportId);
        
        // 检查报告权限
        ReportItemVo reportItemVo = reportItemService.getReportItemById(reportId);
        if (reportItemVo == null) {
            return ApiResult.fail("报告不存在");
        }
        
        // 检查权限
        boolean hasPermission = doctorId.equals(reportItemVo.getDoctorId()) || 
                               (reportItemVo.getDoctorId() != null && reportItemVo.getDoctorId() == 0);
        
        if (!hasPermission) {
            return ApiResult.fail("无权更新此报告");
        }
        
        // 如果医生不是报告的负责人，但有权限查看（即doctorId为0的情况），则更新报告的doctorId为当前医生
        if (reportItemVo.getDoctorId() != null && reportItemVo.getDoctorId() == 0) {
            reportItemService.updateReportItemDoctor(reportId, doctorId);
        }
        
        // 设置报告ID和医生ID
        reportItemDetailDto.setReportId(reportId);
        reportItemDetailDto.setDoctorId(doctorId);
        
        // 更新报告明细
        boolean result = reportItemDetailService.updateReportItemDetail(reportItemDetailDto);
        
        if (result) {
            return ApiResult.success("更新体检报告检查项明细成功");
        } else {
            return ApiResult.fail("更新体检报告检查项明细失败");
        }
    }

    /**
     * 批量更新体检报告检查项及明细
     *
     * @param request HTTP请求对象，用于获取请求头中的token
     * @param requestData 包含报告ID和检查项明细数据的请求体
     * @return 更新结果
     */
    @Operation(summary = "批量更新体检报告检查项及明细")
    @PostMapping("/update")
    public ApiResult<?> batchUpdateReportItems(
            HttpServletRequest request,
            @RequestBody Map<String, Object> requestData) {
        
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
        Long reportId = Long.parseLong(requestData.get("reportId").toString());
        String conclusion = requestData.get("conclusion") != null ? requestData.get("conclusion").toString() : "";
        List<Map<String, Object>> checkItems = (List<Map<String, Object>>) requestData.get("checkItems");
        
        log.info("批量更新体检报告检查项及明细，doctorId: {}, reportId: {}, checkItemsCount: {}", 
                doctorId, reportId, checkItems != null ? checkItems.size() : 0);
        
        // 检查报告权限
        ReportItemVo reportItemVo = reportItemService.getReportItemById(reportId);
        if (reportItemVo == null) {
            return ApiResult.fail("报告不存在");
        }
        
        // 检查权限
        boolean hasPermission = doctorId.equals(reportItemVo.getDoctorId()) || 
                               (reportItemVo.getDoctorId() != null && reportItemVo.getDoctorId() == 0);
        
        if (!hasPermission) {
            return ApiResult.fail("无权更新此报告");
        }
        
        // 如果医生不是报告的负责人，但有权限查看（即doctorId为0的情况），则更新报告的doctorId为当前医生
        if (reportItemVo.getDoctorId() != null && reportItemVo.getDoctorId() == 0) {
            reportItemService.updateReportItemDoctor(reportId, doctorId);
        }
        
        // 更新报告主表结论
        ReportItemDto reportItemDto = new ReportItemDto();
        reportItemDto.setId(reportId);
        reportItemDto.setConclusion(conclusion);
        reportItemService.updateReportItem(reportItemDto);
        
        // 批量更新检查项明细
        boolean allSuccess = true;
        if (checkItems != null && !checkItems.isEmpty()) {
            for (Map<String, Object> checkItem : checkItems) {
                List<Map<String, Object>> details = (List<Map<String, Object>>) checkItem.get("details");
                if (details != null && !details.isEmpty()) {
                    for (Map<String, Object> detail : details) {
                        Long detailId = Long.parseLong(detail.get("id").toString());
                        String value = detail.get("value") != null ? detail.get("value").toString() : "";
                        Boolean isAbnormal = (Boolean) detail.get("isAbnormal");
                        
                        ReportItemDetailDto detailDto = new ReportItemDetailDto();
                        detailDto.setId(detailId);
                        detailDto.setReportId(reportId);
                        detailDto.setDoctorId(doctorId);
                        detailDto.setValue(value);
                        detailDto.setIsAbnormal(isAbnormal ? 1 : 0);
                        
                        try {
                            boolean success = reportItemDetailService.updateReportItemDetail(detailDto);
                            if (!success) {
                                allSuccess = false;
                                log.error("更新检查项明细失败，detailId: {}", detailId);
                            }
                        } catch (Exception e) {
                            allSuccess = false;
                            log.error("更新检查项明细异常，detailId: {}", detailId, e);
                        }
                    }
                }
            }
        }
        
        if (allSuccess) {
            return ApiResult.success("批量更新体检报告检查项及明细成功");
        } else {
            return ApiResult.fail("部分检查项明细更新失败");
        }
    }
} 