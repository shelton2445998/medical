package com.forth.medicalreserve.service.impl;

import com.forth.medicalreserve.entity.Hospital;
import com.forth.medicalreserve.entity.Orders;
import com.forth.medicalreserve.entity.SetMeal;
import com.forth.medicalreserve.entity.Users;
import com.forth.medicalreserve.repository.HospitalRepository;
import com.forth.medicalreserve.repository.OrdersRepository;
import com.forth.medicalreserve.repository.SetMealRepository;
import com.forth.medicalreserve.repository.UsersRepository;
import com.forth.medicalreserve.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersRepository ordersRepository;
    
    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private HospitalRepository hospitalRepository;
    
    @Autowired
    private SetMealRepository setMealRepository;
    
    // 定义可用的时间段
    private static final List<String> TIME_SLOTS = Arrays.asList(
            "08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-12:00",
            "14:00-15:00", "15:00-16:00", "16:00-17:00");
    
    @Override
    @Transactional
    public Orders createOrder(Integer userId, Integer packageId, Integer hospitalId, Date appointmentDate, String timeSlot) {
        // 校验用户是否存在
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 校验体检套餐是否存在
        SetMeal setMeal = setMealRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("体检套餐不存在"));
        
        // 校验医院是否存在
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException("医院不存在"));
        
        // 校验时间段是否有效
        if (!TIME_SLOTS.contains(timeSlot)) {
            throw new RuntimeException("无效的时间段");
        }
        
        // 校验预约日期是否有效（不能预约过去的日期）
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today = calendar.getTime();
        
        if (appointmentDate.before(today)) {
            throw new RuntimeException("不能预约过去的日期");
        }
        
        // 校验当前时间段是否已满
        Long count = countAppointment(hospitalId, appointmentDate, timeSlot);
        // 假设每个时间段最多预约10人
        if (count >= 10) {
            throw new RuntimeException("该时间段预约已满");
        }
        
        // 创建订单
        Orders order = new Orders();
        order.setUserId(userId);
        order.setPackageId(packageId);
        order.setHospitalId(hospitalId);
        order.setAppointmentDate(appointmentDate);
        order.setTimeSlot(timeSlot);
        order.setStatus(1); // 待支付
        
        Date now = new Date();
        order.setCreateTime(now);
        order.setUpdateTime(now);
        
        return ordersRepository.save(order);
    }

    @Override
    @Transactional
    public boolean cancelOrder(Integer orderId, Integer userId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        // 校验用户权限
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }
        
        // 校验订单状态（只有待支付和已支付的订单才能取消）
        if (order.getStatus() != 1 && order.getStatus() != 2) {
            throw new RuntimeException("当前订单状态不可取消");
        }
        
        // 更新订单状态为已取消
        order.setStatus(0);
        order.setUpdateTime(new Date());
        ordersRepository.save(order);
        
        return true;
    }

    @Override
    @Transactional
    public Orders updateOrder(Integer orderId, Integer userId, Date appointmentDate, String timeSlot) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        // 校验用户权限
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }
        
        // 校验订单状态（只有待支付和已支付的订单才能修改）
        if (order.getStatus() != 1 && order.getStatus() != 2) {
            throw new RuntimeException("当前订单状态不可修改");
        }
        
        // 校验时间段是否有效
        if (!TIME_SLOTS.contains(timeSlot)) {
            throw new RuntimeException("无效的时间段");
        }
        
        // 校验预约日期是否有效（不能预约过去的日期）
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today = calendar.getTime();
        
        if (appointmentDate.before(today)) {
            throw new RuntimeException("不能预约过去的日期");
        }
        
        // 校验当前时间段是否已满（如果修改了时间段）
        if (!order.getAppointmentDate().equals(appointmentDate) || !order.getTimeSlot().equals(timeSlot)) {
            Long count = countAppointment(order.getHospitalId(), appointmentDate, timeSlot);
            // 假设每个时间段最多预约10人
            if (count >= 10) {
                throw new RuntimeException("该时间段预约已满");
            }
        }
        
        // 更新订单
        order.setAppointmentDate(appointmentDate);
        order.setTimeSlot(timeSlot);
        order.setUpdateTime(new Date());
        
        return ordersRepository.save(order);
    }

    @Override
    @Transactional
    public Orders payOrder(Integer orderId, Integer userId, String payMethod, Double payAmount) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        // 校验用户权限
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }
        
        // 校验订单状态（只有待支付的订单才能支付）
        if (order.getStatus() != 1) {
            throw new RuntimeException("当前订单状态不可支付");
        }
        
        // 获取套餐价格
        SetMeal setMeal = setMealRepository.findById(order.getPackageId())
                .orElseThrow(() -> new RuntimeException("体检套餐不存在"));
        
        // 校验支付金额
        Double price = setMeal.getDiscountPrice() != null ? 
                setMeal.getDiscountPrice().doubleValue() : 
                setMeal.getPrice().doubleValue();
        if (!Objects.equals(price, payAmount)) {
            throw new RuntimeException("支付金额不正确");
        }
        
        // TODO: 调用支付接口
        
        // 更新订单状态为已支付
        order.setStatus(2);
        order.setUpdateTime(new Date());
        
        return ordersRepository.save(order);
    }

    @Override
    @Transactional
    public Orders completeOrder(Integer orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        // 校验订单状态（只有已支付的订单才能完成）
        if (order.getStatus() != 2) {
            throw new RuntimeException("当前订单状态不可完成");
        }
        
        // 更新订单状态为已完成
        order.setStatus(3);
        order.setUpdateTime(new Date());
        
        return ordersRepository.save(order);
    }

    @Override
    public Orders findById(Integer orderId) {
        return ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    @Override
    public List<Orders> findByUserId(Integer userId) {
        return ordersRepository.findByUserId(userId);
    }

    @Override
    public List<Orders> findByHospitalId(Integer hospitalId) {
        return ordersRepository.findByHospitalId(hospitalId);
    }

    @Override
    public Long countAppointment(Integer hospitalId, Date appointmentDate, String timeSlot) {
        return ordersRepository.countByHospitalIdAndAppointmentDateAndTimeSlot(hospitalId, appointmentDate, timeSlot);
    }

    @Override
    public Map<String, Boolean> getTimeSlotAvailability(Integer hospitalId, Date appointmentDate, int maxAppointments) {
        Map<String, Boolean> availability = new HashMap<>();
        
        for (String timeSlot : TIME_SLOTS) {
            Long count = countAppointment(hospitalId, appointmentDate, timeSlot);
            availability.put(timeSlot, count < maxAppointments);
        }
        
        return availability;
    }
} 