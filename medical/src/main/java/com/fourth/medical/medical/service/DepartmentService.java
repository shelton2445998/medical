package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.DepartmentDto;
import com.fourth.medical.medical.entity.Department;
import com.fourth.medical.medical.query.DepartmentQuery;
import com.fourth.medical.medical.vo.DepartmentVo;
import com.fourth.medical.medical.query.AppDepartmentQuery;
import com.fourth.medical.medical.vo.AppDepartmentVo;


/**
 * 科室 服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 添加科室
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addDepartment(DepartmentDto dto);

    /**
     * 修改科室
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateDepartment(DepartmentDto dto);

    /**
     * 删除科室
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteDepartment(Long id);

    /**
     * 科室详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    DepartmentVo getDepartmentById(Long id);

    /**
     * 科室分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<DepartmentVo> getDepartmentPage(DepartmentQuery query);

    /**
     * App科室详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppDepartmentVo getAppDepartmentById(Long id);

    /**
     * App科室分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppDepartmentVo> getAppDepartmentPage(AppDepartmentQuery query);

}
