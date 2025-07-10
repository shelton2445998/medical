package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.DoctorDto;
import com.fourth.medical.medical.entity.Doctor;
import com.fourth.medical.medical.mapper.DoctorMapper;
import com.fourth.medical.medical.query.DoctorQuery;
import com.fourth.medical.medical.service.DoctorService;
import com.fourth.medical.medical.vo.DoctorVo;
import com.fourth.medical.medical.query.AppDoctorQuery;
import com.fourth.medical.medical.vo.AppDoctorVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * 医生 服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements DoctorService {

    @Autowired
    private DoctorMapper doctorMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addDoctor(DoctorDto dto) {
        Doctor doctor = new Doctor();
        BeanUtils.copyProperties(dto, doctor);
        return save(doctor);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateDoctor(DoctorDto dto) {
        Long id = dto.getId();
        Doctor doctor = getById(id);
        if (doctor == null) {
            throw new BusinessException("医生不存在");
        }
        BeanUtils.copyProperties(dto, doctor);
        return updateById(doctor);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteDoctor(Long id) {
        return removeById(id);
    }

    @Override
    public DoctorVo getDoctorById(Long id) {
        return doctorMapper.getDoctorById(id);
    }

    @Override
    public Paging<DoctorVo> getDoctorPage(DoctorQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<DoctorVo> list = doctorMapper.getDoctorPage(query);
        Paging<DoctorVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppDoctorVo getAppDoctorById(Long id) {
        return doctorMapper.getAppDoctorById(id);
    }

    @Override
    public Paging<AppDoctorVo> getAppDoctorPage(AppDoctorQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<AppDoctorVo> list = doctorMapper.getAppDoctorPage(query);
        Paging<AppDoctorVo> paging = new Paging<>(list);
        return paging;
    }

}
