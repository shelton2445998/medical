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

}
