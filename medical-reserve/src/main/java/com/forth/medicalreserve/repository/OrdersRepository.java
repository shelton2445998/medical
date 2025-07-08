package com.forth.medicalreserve.repository;

import com.forth.medicalreserve.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 体检预约订单数据访问层
 */
@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    
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
     * 根据套餐ID查询订单列表
     * @param packageId 套餐ID
     * @return 订单列表
     */
    List<Orders> findByPackageId(Integer packageId);
    
    /**
     * 根据状态查询订单列表
     * @param status 状态
     * @return 订单列表
     */
    List<Orders> findByStatus(Integer status);
    
    /**
     * 根据用户ID和状态查询订单列表
     * @param userId 用户ID
     * @param status 状态
     * @return 订单列表
     */
    List<Orders> findByUserIdAndStatus(Integer userId, Integer status);
    
    /**
     * 根据预约日期查询订单列表
     * @param appointmentDate 预约日期
     * @return 订单列表
     */
    List<Orders> findByAppointmentDate(Date appointmentDate);
    
    /**
     * 根据医院ID、预约日期和时间段查询订单数量
     * @param hospitalId 医院ID
     * @param appointmentDate 预约日期
     * @param timeSlot 时间段
     * @return 订单数量
     */
    @Query("SELECT COUNT(o) FROM Orders o WHERE o.hospitalId = ?1 AND o.appointmentDate = ?2 AND o.timeSlot = ?3 AND o.status IN (1, 2, 3)")
    Long countByHospitalIdAndAppointmentDateAndTimeSlot(Integer hospitalId, Date appointmentDate, String timeSlot);
    
    /**
     * 根据用户ID和日期范围查询订单列表
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 订单列表
     */
    @Query("SELECT o FROM Orders o WHERE o.userId = ?1 AND o.appointmentDate BETWEEN ?2 AND ?3")
    List<Orders> findByUserIdAndAppointmentDateBetween(Integer userId, Date startDate, Date endDate);
} 