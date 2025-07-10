package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.HospitalDto;
import com.fourth.medical.medical.entity.Hospital;
import com.fourth.medical.medical.mapper.HospitalMapper;
import com.fourth.medical.medical.query.HospitalQuery;
import com.fourth.medical.medical.service.HospitalService;
import com.fourth.medical.medical.vo.HospitalVo;
import com.fourth.medical.medical.query.AppHospitalQuery;
import com.fourth.medical.medical.vo.AppHospitalVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.ArrayList;
import java.util.List;

/**
 * 医院 服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, Hospital> implements HospitalService {

    @Autowired
    private HospitalMapper hospitalMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addHospital(HospitalDto dto) {
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(dto, hospital);
        return save(hospital);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateHospital(HospitalDto dto) {
        Long id = dto.getId();
        Hospital hospital = getById(id);
        if (hospital == null) {
            throw new BusinessException("医院不存在");
        }
        BeanUtils.copyProperties(dto, hospital);
        return updateById(hospital);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteHospital(Long id) {
        return removeById(id);
    }

    @Override
    public HospitalVo getHospitalById(Long id) {
        return hospitalMapper.getHospitalById(id);
    }

    @Override
    public Paging<HospitalVo> getHospitalPage(HospitalQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<HospitalVo> list = hospitalMapper.getHospitalPage(query);
        Paging<HospitalVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppHospitalVo getAppHospitalById(Long id) {
        return hospitalMapper.getAppHospitalById(id);
    }

    @Override
    public Paging<AppHospitalVo> getAppHospitalPage(AppHospitalQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<AppHospitalVo> list = hospitalMapper.getAppHospitalPage(query);
        Paging<AppHospitalVo> paging = new Paging<>(list);
        return paging;
    }
    
    @Override
    public List<AppHospitalVo> getRecommendHospitals() {
        // 获取推荐的医院，这里简单实现为获取状态为启用的前5家医院
        LambdaQueryWrapper<Hospital> wrapper = Wrappers.lambdaQuery(Hospital.class)
            .eq(Hospital::getStatus, true)
            .orderByDesc(Hospital::getCreateTime)
            .last("limit 5");
            
        List<Hospital> hospitals = list(wrapper);
        
        // 转换为AppHospitalVo列表
        List<AppHospitalVo> result = new ArrayList<>();
        for (Hospital hospital : hospitals) {
            AppHospitalVo vo = hospitalMapper.getAppHospitalById(hospital.getId());
            if (vo != null) {
                result.add(vo);
            }
        }
        
        return result;
    }

}
