package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.AppOrdersDto;
import com.fourth.medical.medical.query.AppOrdersQuery;
import com.fourth.medical.medical.service.OrdersService;
import com.fourth.medical.medical.service.ReportService;
import com.fourth.medical.medical.service.ReportItemService;
import com.fourth.medical.medical.service.DoctorScheduleService;
import com.fourth.medical.medical.vo.AppOrdersVo;
import com.fourth.medical.auth.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * App体检预约订单 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App体检预约")
@RequestMapping("/app/appointment")
public class AppOrdersController {

    @Autowired
    private OrdersService ordersService;
    
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private ReportItemService reportItemService;
    
    @Autowired
    private DoctorScheduleService doctorScheduleService;

    /**
     * 创建体检预约
     *
     * @param dto
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary = "创建体检预约")
    @PostMapping("/create")
    public ApiResult<AppOrdersVo> createAppointment(@Valid @RequestBody AppOrdersDto dto, HttpServletRequest request) {
        log.info("创建体检预约：{}", dto);
        // 从请求中获取token
        String token = TokenUtil.getToken(request);
        log.info("获取到token: {}", token);
        
        // 创建订单
        AppOrdersVo result = ordersService.createAppOrders(dto, token);
        
        // 根据订单信息创建体检报告
        if (result != null && result.getId() != null) {
            // 如果订单创建成功且有检查项目，则创建对应的report和report_item
            if (result.getCheckitemIds() != null && !result.getCheckitemIds().isEmpty()) {
                // 分配医生 - 根据科室和当天值班情况分配医生
                Long doctorId = doctorScheduleService.assignDoctorForOrder(
                        result.getId(), 
                        result.getHospitalId(), 
                        result.getAppointmentDate(), 
                        result.getCheckitemIds());
                
                // 创建report记录
                Long reportId = reportService.createReportForOrder(
                        result.getId(),
                        result.getUserId(), 
                        result.getCheckitemIds(),
                        doctorId);
                
                // 创建report_item记录
                if (reportId != null) {
                    reportItemService.createReportItemsForReport(
                            reportId, 
                            result.getId(), 
                            result.getUserId(), 
                            result.getCheckitemIds(), 
                            doctorId);
                }
            }
        }
        
        return ApiResult.success(result);
    }

    /**
     * 获取体检预约详情
     *
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检预约详情")
    @GetMapping("/detail/{id}")
    public ApiResult<AppOrdersVo> getAppointmentDetail(@PathVariable Long id, HttpServletRequest request) {
        log.info("获取体检预约详情：{}", id);
        // 从请求中获取token
        String token = TokenUtil.getToken(request);
        log.info("获取到token: {}", token);
        
        AppOrdersVo appOrdersVo = ordersService.getAppOrdersById(id, token);
        return ApiResult.success(appOrdersVo);
    }

    /**
     * 获取体检预约列表
     *
     * @param query
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检预约列表")
    @GetMapping("/list")
    public ApiResult<AppOrdersVo> getAppointmentList(@Valid AppOrdersQuery query, HttpServletRequest request) {
        log.info("获取体检预约列表：{}", query);
        // 从请求中获取token
        String token = TokenUtil.getToken(request);
        log.info("获取到token: {}", token);
        
        Paging<AppOrdersVo> paging = ordersService.getAppOrdersPage(query, token);
        return ApiResult.success(paging);
    }

    /**
     * 取消体检预约
     *
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary = "取消体检预约")
    @PutMapping("/cancel/{id}")
    public ApiResult cancelAppointment(@PathVariable Long id, HttpServletRequest request) {
        log.info("取消体检预约：{}", id);
        // 从请求中获取token
        String token = TokenUtil.getToken(request);
        log.info("获取到token: {}", token);
        
        boolean result = ordersService.cancelAppOrders(id, token);
        return ApiResult.result(result);
    }

}
