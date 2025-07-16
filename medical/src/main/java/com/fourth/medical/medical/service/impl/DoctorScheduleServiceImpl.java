package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.DoctorScheduleDto;

import com.fourth.medical.medical.entity.DoctorSchedule;
import com.fourth.medical.medical.entity.Orders;

import com.fourth.medical.medical.mapper.DoctorScheduleMapper;
import com.fourth.medical.medical.mapper.OrdersMapper;
import com.fourth.medical.medical.query.DoctorScheduleQuery;
import com.fourth.medical.medical.service.CheckitemDetailService;
import com.fourth.medical.medical.service.DoctorScheduleService;
import com.fourth.medical.medical.service.OrdersService;
import com.fourth.medical.medical.service.ReportService;
import com.fourth.medical.medical.vo.DoctorScheduleVo;
import com.fourth.medical.medical.query.AppDoctorScheduleQuery;
import com.fourth.medical.medical.vo.AppDoctorScheduleVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 医生排班 服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class DoctorScheduleServiceImpl extends ServiceImpl<DoctorScheduleMapper, DoctorSchedule> implements DoctorScheduleService {

    @Autowired
    private DoctorScheduleMapper doctorScheduleMapper;
    
    @Autowired
    private OrdersMapper ordersMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addDoctorSchedule(DoctorScheduleDto dto) {
        DoctorSchedule doctorSchedule = new DoctorSchedule();
        BeanUtils.copyProperties(dto, doctorSchedule);
        return save(doctorSchedule);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateDoctorSchedule(DoctorScheduleDto dto) {
        Long id = dto.getId();
        DoctorSchedule doctorSchedule = getById(id);
        if (doctorSchedule == null) {
            throw new BusinessException("医生排班不存在");
        }
        BeanUtils.copyProperties(dto, doctorSchedule);
        return updateById(doctorSchedule);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteDoctorSchedule(Long id) {
        return removeById(id);
    }

    @Override
    public DoctorScheduleVo getDoctorScheduleById(Long id) {
        return doctorScheduleMapper.getDoctorScheduleById(id);
    }

    @Override
    public Paging<DoctorScheduleVo> getDoctorSchedulePage(DoctorScheduleQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<DoctorScheduleVo> list = doctorScheduleMapper.getDoctorSchedulePage(query);
        Paging<DoctorScheduleVo> paging = new Paging<>(list);
        return paging;
    }
    
    @Override
    public List<DoctorScheduleVo> getDoctorScheduleListByDoctorId(Long doctorId, DoctorScheduleQuery query) {
        return doctorScheduleMapper.getDoctorScheduleListByDoctorId(doctorId, query);
    }

    @Override
    public AppDoctorScheduleVo getAppDoctorScheduleById(Long id) {
        return doctorScheduleMapper.getAppDoctorScheduleById(id);
    }

    @Override
    public Paging<AppDoctorScheduleVo> getAppDoctorSchedulePage(AppDoctorScheduleQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<AppDoctorScheduleVo> list = doctorScheduleMapper.getAppDoctorSchedulePage(query);
        Paging<AppDoctorScheduleVo> paging = new Paging<>(list);
        return paging;
    }

    /**
     * 根据订单信息和检查项目分配医生
     *
     * @param orderId 订单ID
     * @param hospitalId 医院ID
     * @param appointmentDate 预约日期
     * @param checkitemIds 检查项ID列表
     * @return 分配的医生ID，如果没有合适的医生则返回null
     */
    @Override
    public Long assignDoctorForOrder(Long orderId, Long hospitalId, Date appointmentDate, String checkitemIds) {
        log.info("分配医生，订单ID：{}，医院ID：{}，预约日期：{}，检查项ID：{}", orderId, hospitalId, appointmentDate, checkitemIds);
        
        try {
            // 1. 解析检查项ID，确定相关科室
            List<Long> checkItemIdList = Arrays.stream(checkitemIds.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            
            // 2. 查询检查项所属科室
            List<Long> departmentIds = baseMapper.getCheckItemDepartments(checkItemIdList);
            
            if (departmentIds == null || departmentIds.isEmpty()) {
                log.warn("未找到检查项对应的科室，无法分配医生");
                return null;
            }
            
            // 3. 根据医院ID、预约日期、科室ID查询当天值班医生
            List<Map<String, Object>> availableDoctors = baseMapper.getAvailableDoctorsForSchedule(
                    hospitalId, 
                    appointmentDate,
                    departmentIds);
            
            if (availableDoctors == null || availableDoctors.isEmpty()) {
                log.warn("当天没有可用的值班医生，尝试寻找排班日期最近的医生");
                
                // 4. 尝试查找最近的排班医生
                List<Map<String, Object>> nearestDoctors = baseMapper.getNearestAvailableDoctors(
                        hospitalId,
                        appointmentDate,
                        departmentIds);
                
                if (nearestDoctors == null || nearestDoctors.isEmpty()) {
                    log.warn("未找到任何可用的排班医生");
                    return null;
                }
                
                // 找出与预约日期相差最小的医生
                Map<String, Object> nearestDoctor = nearestDoctors.get(0);
                Long doctorId = (Long) nearestDoctor.get("doctorId");
                log.info("找到最近的可用医生，医生ID：{}，排班日期：{}", doctorId, nearestDoctor.get("workDate"));
                
                // 更新订单中的医生ID
                baseMapper.updateOrderDoctor(orderId, doctorId);
                
                return doctorId;
            }
            
            // 4. 找出当前预约人数最少的医生
            Map<String, Object> selectedDoctor = availableDoctors.stream()
                    .min(Comparator.comparing(doctor -> (Long) doctor.get("appointmentCount")))
                    .orElse(null);
            
            if (selectedDoctor == null) {
                log.warn("无法找到合适的医生");
                return null;
            }
            
            Long doctorId = (Long) selectedDoctor.get("doctorId");
            log.info("为订单分配了医生，医生ID：{}", doctorId);
            
            // 5. 更新订单中的医生ID
            baseMapper.updateOrderDoctor(orderId, doctorId);
            
            return doctorId;
        } catch (Exception e) {
            log.error("分配医生发生异常", e);
            return null;
        }
    }

    @Override
    public Boolean checkDoctorHasScheduleToday(Long doctorId) {
        log.info("检查医生[{}]今日是否有排班", doctorId);
        try {
            // 获取今天的日期
            LocalDate today = LocalDate.now();
            Date todayDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            // 获取明天的日期（今天的结束）
            Date tomorrowDate = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            // 查询今天是否有排班
            LambdaQueryWrapper<DoctorSchedule> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DoctorSchedule::getDoctorId, doctorId)
                   .ge(DoctorSchedule::getWorkDate, todayDate)
                   .lt(DoctorSchedule::getWorkDate, tomorrowDate);
            
            Long count = doctorScheduleMapper.selectCount(wrapper);
            return count != null && count > 0;
        } catch (Exception e) {
            log.error("检查医生今日是否有排班出错", e);
            return false;
        }
    }

    @Override
    public Integer countTodayAppointmentsByDoctorId(Long doctorId) {
        log.info("获取医生[{}]今日预约数量", doctorId);
        try {
            // 获取今天的日期
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date todayStart = calendar.getTime();
            
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date tomorrowStart = calendar.getTime();
            
            // 查询今天的预约数量
            LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Orders::getDoctorId, doctorId)
                   .ge(Orders::getAppointmentDate, todayStart)
                   .lt(Orders::getAppointmentDate, tomorrowStart);
            
            Long count = ordersMapper.selectCount(wrapper);
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            log.error("获取医生今日预约数量出错", e);
            return 0;
        }
    }

    @Override
    public Integer countWeekAppointmentsByDoctorId(Long doctorId) {
        log.info("获取医生[{}]本周预约数量", doctorId);
        try {
            // 获取本周的开始日期和结束日期
            LocalDate now = LocalDate.now();
            LocalDate startOfWeek = now.with(java.time.DayOfWeek.MONDAY);
            LocalDate endOfWeek = now.with(java.time.DayOfWeek.SUNDAY);
            
            // 转换为Date类型
            Date startDate = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(endOfWeek.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            // 查询本周预约数量
            Integer count = ordersMapper.countAppointmentsByDoctorIdAndDateRange(doctorId, startDate, endDate);
            log.info("医生[{}]本周预约数量：{}", doctorId, count);
            return count;
        } catch (Exception e) {
            log.error("获取医生本周预约数量失败", e);
            return 0;
        }
    }
    
    /**
     * 获取检查项与科室的映射关系
     *
     * @param checkItemIds 检查项ID列表
     * @return 检查项ID到科室ID的映射
     */
    @Override
    public Map<Long, Long> getCheckItemDepartmentMap(List<Long> checkItemIds) {
        log.info("获取检查项与科室的映射关系，检查项ID：{}", checkItemIds);
        try {
            // 调用mapper查询检查项与科室的关系
            List<Map<String, Object>> checkItemDepartments = baseMapper.getCheckItemDepartmentMapping(checkItemIds);
            
            // 将结果转换为Map
            Map<Long, Long> result = new HashMap<>();
            for (Map<String, Object> item : checkItemDepartments) {
                Long checkItemId = (Long) item.get("checkItemId");
                Long departmentId = (Long) item.get("departmentId");
                result.put(checkItemId, departmentId);
            }
            
            log.info("检查项与科室映射关系：{}", result);
            return result;
        } catch (Exception e) {
            log.error("获取检查项与科室映射关系失败", e);
            return new HashMap<>();
        }
    }
    
    /**
     * 根据科室分配医生
     *
     * @param hospitalId 医院ID
     * @param appointmentDate 预约日期
     * @param departmentIds 科室ID列表
     * @return 科室ID到医生ID的映射
     */
    @Override
    public Map<Long, Long> assignDoctorsForDepartments(Long hospitalId, Date appointmentDate, List<Long> departmentIds) {
        log.info("根据科室分配医生，医院ID：{}，预约日期：{}，科室ID：{}", hospitalId, appointmentDate, departmentIds);
        
        Map<Long, Long> departmentDoctorMap = new HashMap<>();
        
        try {
            for (Long departmentId : departmentIds) {
                // 根据医院ID、预约日期、科室ID查询当天值班医生
                List<Map<String, Object>> availableDoctors = baseMapper.getAvailableDoctorsForDepartment(
                        hospitalId, 
                        appointmentDate,
                        departmentId);
                
                if (availableDoctors == null || availableDoctors.isEmpty()) {
                    log.warn("科室[{}]当天没有可用的值班医生，尝试寻找排班日期最近的医生", departmentId);
                    
                    // 尝试查找最近的排班医生
                    List<Map<String, Object>> nearestDoctors = baseMapper.getNearestAvailableDoctorsForDepartment(
                            hospitalId,
                            appointmentDate,
                            departmentId);
                    
                    if (nearestDoctors != null && !nearestDoctors.isEmpty()) {
                        // 找出与预约日期相差最小的医生
                        Map<String, Object> nearestDoctor = nearestDoctors.get(0);
                        Long doctorId = (Long) nearestDoctor.get("doctorId");
                        log.info("科室[{}]找到最近的可用医生，医生ID：{}，排班日期：{}", 
                                departmentId, doctorId, nearestDoctor.get("workDate"));
                        
                        departmentDoctorMap.put(departmentId, doctorId);
                    } else {
                        log.warn("科室[{}]未找到任何可用的排班医生", departmentId);
                        // 如果找不到医生，设置为null
                        departmentDoctorMap.put(departmentId, null);
                    }
                } else {
                    // 找出当前预约人数最少的医生
                    Map<String, Object> selectedDoctor = availableDoctors.stream()
                            .min(Comparator.comparing(doctor -> (Long) doctor.get("appointmentCount")))
                            .orElse(null);
                    
                    if (selectedDoctor != null) {
                        Long doctorId = (Long) selectedDoctor.get("doctorId");
                        log.info("科室[{}]分配了医生，医生ID：{}", departmentId, doctorId);
                        departmentDoctorMap.put(departmentId, doctorId);
                    } else {
                        log.warn("科室[{}]无法找到合适的医生", departmentId);
                        departmentDoctorMap.put(departmentId, null);
                    }
                }
            }
            
            log.info("科室医生分配结果：{}", departmentDoctorMap);
            return departmentDoctorMap;
        } catch (Exception e) {
            log.error("分配科室医生发生异常", e);
            return departmentDoctorMap;
        }
    }
}
