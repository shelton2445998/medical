package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.AppOrdersDto;
import com.fourth.medical.medical.query.AppOrdersQuery;
import com.fourth.medical.medical.service.OrdersService;
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
        
        AppOrdersVo result = ordersService.createAppOrders(dto, token);
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
