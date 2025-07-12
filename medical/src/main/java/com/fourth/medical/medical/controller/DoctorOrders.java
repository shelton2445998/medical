package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.OrdersDto;
import com.fourth.medical.medical.query.OrdersQuery;
import com.fourth.medical.medical.service.OrdersService;
import com.fourth.medical.medical.vo.OrdersVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 体检预约订单 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "体检预约订单")
@RequestMapping("/doctor/appointment")
public class DoctorOrders {

    @Autowired
    private OrdersService ordersService;

    /**
     * 添加体检预约订单
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加体检预约订单")
    @PostMapping("/add")
    @Permission("orders:add")
    public ApiResult addOrders(@Valid @RequestBody OrdersDto dto) {
        log.info("添加体检预约订单：{}", dto);
        boolean flag = ordersService.addOrders(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改体检预约订单
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改体检预约订单")
    @PutMapping("/update")
    @Permission("orders:update")
    public ApiResult updateOrders(@Valid @RequestBody OrdersDto dto) {
        log.info("修改体检预约订单：{}", dto);
        boolean flag = ordersService.updateOrders(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除体检预约订单
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除体检预约订单")
    @DeleteMapping("/delete/{id}")
    @Permission("orders:delete")
    public ApiResult deleteOrders(@PathVariable Long id) {
        log.info("删除体检预约订单：{}", id);
        boolean flag = ordersService.deleteOrders(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取体检预约订单详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检预约订单详情")
    @GetMapping("/detail/{appointmentId}")
    @Permission("orders:info")
    public ApiResult<OrdersVo> getOrders(@PathVariable("appointmentId") Long id) {
        log.info("获取体检预约订单详情：{}", id);
        OrdersVo ordersVo = ordersService.getOrdersById(id);
        return ApiResult.success(ordersVo);
    }

    /**
     * 获取体检预约订单列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检预约订单列表")
    @GetMapping("/list")
    @Permission("orders:page")
    public ApiResult<OrdersVo> getOrdersList(@Valid OrdersQuery query) {
        log.info("获取体检预约订单列表：{}", query);
        Paging<OrdersVo> paging = ordersService.getOrdersPage(query);
        return ApiResult.success(paging);
    }

    /**
     * 更新预约状态
     *
     * @param id
     * @param status
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "更新预约状态")
    @PutMapping("/status")
    @Permission("orders:update")
    public ApiResult updateOrdersStatus(@RequestParam Long id, @RequestParam Integer status) {
        log.info("更新预约状态：id={}, status={}", id, status);
        OrdersDto dto = new OrdersDto();
        dto.setId(id);
        dto.setStatus(status);
        boolean flag = ordersService.updateOrders(dto);
        return ApiResult.result(flag);
    }

}
