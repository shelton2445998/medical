package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.DepartmentDto;
import com.fourth.medical.medical.entity.Department;
import com.fourth.medical.medical.mapper.DepartmentMapper;
import com.fourth.medical.medical.query.DepartmentQuery;
import com.fourth.medical.medical.service.DepartmentService;
import com.fourth.medical.medical.vo.DepartmentVo;
import com.fourth.medical.medical.query.AppDepartmentQuery;
import com.fourth.medical.medical.vo.AppDepartmentVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * 科室 服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addDepartment(DepartmentDto dto) {
        Department department = new Department();
        BeanUtils.copyProperties(dto, department);
        return save(department);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateDepartment(DepartmentDto dto) {
        Long id = dto.getId();
        Department department = getById(id);
        if (department == null) {
            throw new BusinessException("科室不存在");
        }
        BeanUtils.copyProperties(dto, department);
        return updateById(department);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteDepartment(Long id) {
        return removeById(id);
    }

    @Override
    public DepartmentVo getDepartmentById(Long id) {
        return departmentMapper.getDepartmentById(id);
    }

    @Override
    public Paging<DepartmentVo> getDepartmentPage(DepartmentQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<DepartmentVo> list = departmentMapper.getDepartmentPage(query);
        Paging<DepartmentVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppDepartmentVo getAppDepartmentById(Long id) {
        return departmentMapper.getAppDepartmentById(id);
    }

    @Override
    public Paging<AppDepartmentVo> getAppDepartmentPage(AppDepartmentQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<AppDepartmentVo> list = departmentMapper.getAppDepartmentPage(query);
        Paging<AppDepartmentVo> paging = new Paging<>(list);
        return paging;
    }

}
