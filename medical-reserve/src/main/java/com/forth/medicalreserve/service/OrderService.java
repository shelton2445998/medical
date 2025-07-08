package com.forth.medicalreserve.service;

import com.forth.medicalreserve.entity.Orders;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单服务接口
 */
public interface OrderService {
    
    /**
     * 创建预约订单
     * @param userId 用户ID
     * @param packageId 套餐ID
     * @param hospitalId 医院ID
     * @param appointmentDate 预约日期
     * @param timeSlot 时间段
     * @return 预约订单
     */
    Orders createOrder(Integer userId, Integer packageId, Integer hospitalId, Date appointmentDate, String timeSlot);
    
    /**
     * 取消预约订单
     * @param orderId 订单ID
     * @param userId 用户ID（用于验证权限）
     * @return 是否取消成功
     */
    boolean cancelOrder(Integer orderId, Integer userId);
    
    /**
     * 修改预约订单
     * @param orderId 订单ID
     * @param userId 用户ID（用于验证权限）
     * @param appointmentDate 新的预约日期
     * @param timeSlot 新的时间段
     * @return 修改后的订单
     */
    Orders updateOrder(Integer orderId, Integer userId, Date appointmentDate, String timeSlot);
    
    /**
     * 支付订单
     * @param orderId 订单ID
     * @param userId 用户ID（用于验证权限）
     * @param payMethod 支付方式
     * @param payAmount 支付金额
     * @return 支付后的订单
     */
    Orders payOrder(Integer orderId, Integer userId, String payMethod, Double payAmount);
    
    /**
     * 完成订单
     * @param orderId 订单ID
     * @return 完成后的订单
     */
    Orders completeOrder(Integer orderId);
    
    /**
     * 根据ID查询订单
     * @param orderId 订单ID
     * @return 订单信息
     */
    Orders findById(Integer orderId);
    
    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Orders> findByUserId(Integer userId);
    
    /**
     * 根据医院ID查询订单列表
     * @param hospitalId 医院ID
     * @return 订单列表
     */
    List<Orders> findByHospitalId(Integer hospitalId);
    
    /**
     * 查询某医院某日期某时间段的预约人数
     * @param hospitalId 医院ID
     * @param appointmentDate 预约日期
     * @param timeSlot 时间段
     * @return 预约人数
     */
    Long countAppointment(Integer hospitalId, Date appointmentDate, String timeSlot);
    
    /**
     * 查询某天各时间段的可预约情况
     * @param hospitalId 医院ID
     * @param appointmentDate 预约日期
     * @param maxAppointments 每个时段最大预约数
     * @return 时间段 -> 是否可约的映射
     */
    Map<String, Boolean> getTimeSlotAvailability(Integer hospitalId, Date appointmentDate, int maxAppointments);
} 