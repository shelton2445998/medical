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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;

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
            // 直接使用order中的检查项ID列表
            String checkitemIds = result.getCheckitemIds();
            
            // 如果订单创建成功且有检查项目，则创建对应的report和report_item
            if (StringUtils.isNotBlank(checkitemIds)) {
                // 创建report记录，不设置doctorId
                Long reportId = reportService.createReportForOrder(
                        result.getId(),
                        result.getUserId(), 
                        checkitemIds);
                
                if (reportId != null) {
                    // 解析检查项ID列表
                    List<Long> checkItemIdList = Arrays.stream(checkitemIds.split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                    
                    // 获取检查项与科室的映射关系
                    Map<Long, Long> checkItemDepartmentMap = doctorScheduleService.getCheckItemDepartmentMap(checkItemIdList);
                    
                    // 根据科室分配医生
                    Map<Long, Long> departmentDoctorMap = doctorScheduleService.assignDoctorsForDepartments(
                            result.getHospitalId(),
                            result.getAppointmentDate(),
                            checkItemDepartmentMap.values().stream().distinct().collect(Collectors.toList()));
                    
                    // 为每个检查项创建report_item记录，并根据科室分配医生
                    List<Long> createdItemIds = new ArrayList<>();
                    for (Long checkItemId : checkItemIdList) {
                        // 获取检查项对应的科室
                        Long departmentId = checkItemDepartmentMap.get(checkItemId);
                        // 获取科室对应的医生
                        Long doctorId = departmentDoctorMap.get(departmentId);
                        
                        // 创建report_item记录
                        Long reportItemId = reportItemService.createReportItemForCheckItem(
                                reportId,
                                result.getId(),
                                result.getUserId(),
                                checkItemId,
                                doctorId);
                        
                        if (reportItemId != null) {
                            createdItemIds.add(reportItemId);
                        }
                    }
                    
                    // 更新report表中的report_item_ids字段
                    if (!createdItemIds.isEmpty()) {
                        String reportItemIdsStr = createdItemIds.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(","));
                        
                        reportItemService.updateReportItemIds(reportId, reportItemIdsStr);
                    }
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

    /**
     * 确认支付
     *
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary = "确认支付")
    @PutMapping("/confirmPayment/{id}")
    public ApiResult confirmPayment(@PathVariable Long id, HttpServletRequest request) {
        log.info("确认支付：{}", id);
        // 从请求中获取token
        String token = TokenUtil.getToken(request);
        log.info("获取到token: {}", token);
        
        boolean result = ordersService.confirmPayment(id, token);
        return ApiResult.result(result);
    }
}
