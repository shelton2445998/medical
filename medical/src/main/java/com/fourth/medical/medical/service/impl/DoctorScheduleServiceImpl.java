package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.DoctorScheduleDto;
import com.fourth.medical.medical.entity.DoctorSchedule;
import com.fourth.medical.medical.mapper.DoctorScheduleMapper;
import com.fourth.medical.medical.query.DoctorScheduleQuery;
import com.fourth.medical.medical.service.DoctorScheduleService;
import com.fourth.medical.medical.vo.DoctorScheduleVo;
import com.fourth.medical.medical.query.AppDoctorScheduleQuery;
import com.fourth.medical.medical.vo.AppDoctorScheduleVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                log.warn("当天没有可用的值班医生");
                return null;
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

}
