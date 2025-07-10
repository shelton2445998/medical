package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.Department;
import com.fourth.medical.medical.query.DepartmentQuery;
import com.fourth.medical.medical.vo.DepartmentVo;
import com.fourth.medical.medical.query.AppDepartmentQuery;
import com.fourth.medical.medical.vo.AppDepartmentVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 科室 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-09
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 科室详情
     *
     * @param id
     * @return
     */
    DepartmentVo getDepartmentById(Long id);

    /**
     * 科室分页列表
     *
     * @param query
     * @return
     */
    List<DepartmentVo> getDepartmentPage(DepartmentQuery query);

    /**
     * App科室详情
     *
     * @param id
     * @return
     */
    AppDepartmentVo getAppDepartmentById(Long id);

    /**
     * App科室分页列表
     *
     * @param query
     * @return
     */
    List<AppDepartmentVo> getAppDepartmentPage(AppDepartmentQuery query);

}
