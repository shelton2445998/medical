package com.forth.medicalreserve.controller;

import com.forth.medicalreserve.common.Result;
import com.forth.medicalreserve.entity.Orders;
import com.forth.medicalreserve.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 */
@Api(tags = "体检预约管理")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @ApiOperation("创建预约订单")
    @PostMapping
    public Result<Orders> createOrder(
            @RequestParam Integer userId,
            @RequestParam Integer packageId,
            @RequestParam Integer hospitalId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date appointmentDate,
            @RequestParam String timeSlot) {
        try {
            Orders order = orderService.createOrder(userId, packageId, hospitalId, appointmentDate, timeSlot);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("取消预约订单")
    @PutMapping("/{orderId}/cancel")
    public Result<Boolean> cancelOrder(
            @PathVariable Integer orderId,
            @RequestParam Integer userId) {
        try {
            boolean result = orderService.cancelOrder(orderId, userId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("修改预约订单")
    @PutMapping("/{orderId}")
    public Result<Orders> updateOrder(
            @PathVariable Integer orderId,
            @RequestParam Integer userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date appointmentDate,
            @RequestParam String timeSlot) {
        try {
            Orders order = orderService.updateOrder(orderId, userId, appointmentDate, timeSlot);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("支付订单")
    @PutMapping("/{orderId}/pay")
    public Result<Orders> payOrder(
            @PathVariable Integer orderId,
            @RequestParam Integer userId,
            @RequestParam String payMethod,
            @RequestParam Double payAmount) {
        try {
            Orders order = orderService.payOrder(orderId, userId, payMethod, payAmount);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("完成订单")
    @PutMapping("/{orderId}/complete")
    public Result<Orders> completeOrder(@PathVariable Integer orderId) {
        try {
            Orders order = orderService.completeOrder(orderId);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("根据ID查询订单")
    @GetMapping("/{orderId}")
    public Result<Orders> getOrderById(@PathVariable Integer orderId) {
        try {
            Orders order = orderService.findById(orderId);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("根据用户ID查询订单列表")
    @GetMapping("/user/{userId}")
    public Result<List<Orders>> getOrdersByUserId(@PathVariable Integer userId) {
        try {
            List<Orders> orders = orderService.findByUserId(userId);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("根据医院ID查询订单列表")
    @GetMapping("/hospital/{hospitalId}")
    public Result<List<Orders>> getOrdersByHospitalId(@PathVariable Integer hospitalId) {
        try {
            List<Orders> orders = orderService.findByHospitalId(hospitalId);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("查询某医院某日期某时间段的预约人数")
    @GetMapping("/count")
    public Result<Long> countAppointment(
            @RequestParam Integer hospitalId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date appointmentDate,
            @RequestParam String timeSlot) {
        try {
            Long count = orderService.countAppointment(hospitalId, appointmentDate, timeSlot);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("查询某天各时间段的可预约情况")
    @GetMapping("/availability")
    public Result<Map<String, Boolean>> getTimeSlotAvailability(
            @RequestParam Integer hospitalId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date appointmentDate,
            @RequestParam(defaultValue = "10") int maxAppointments) {
        try {
            Map<String, Boolean> availability = orderService.getTimeSlotAvailability(hospitalId, appointmentDate, maxAppointments);
            return Result.success(availability);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 