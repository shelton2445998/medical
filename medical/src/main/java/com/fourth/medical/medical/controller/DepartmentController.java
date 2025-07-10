package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.DepartmentDto;
import com.fourth.medical.medical.query.DepartmentQuery;
import com.fourth.medical.medical.service.DepartmentService;
import com.fourth.medical.medical.vo.DepartmentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 科室 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "科室")
@RequestMapping("/admin/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 添加科室
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加科室")
    @PostMapping("/addDepartment")
    @Permission("department:add")
    public ApiResult addDepartment(@Valid @RequestBody DepartmentDto dto) {
        log.info("添加科室：{}", dto);
        boolean flag = departmentService.addDepartment(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改科室
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改科室")
    @PostMapping("/updateDepartment")
    @Permission("department:update")
    public ApiResult updateDepartment(@Valid @RequestBody DepartmentDto dto) {
        log.info("修改科室：{}", dto);
        boolean flag = departmentService.updateDepartment(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除科室
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除科室")
    @PostMapping("/deleteDepartment/{id}")
    @Permission("department:delete")
    public ApiResult deleteDepartment(@PathVariable Long id) {
        log.info("删除科室：{}", id);
        boolean flag = departmentService.deleteDepartment(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取科室详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取科室详情")
    @PostMapping("/getDepartment/{id}")
    @Permission("department:info")
    public ApiResult<DepartmentVo> getDepartment(@PathVariable Long id) {
        log.info("获取科室详情：{}", id);
        DepartmentVo departmentVo = departmentService.getDepartmentById(id);
        return ApiResult.success(departmentVo);
    }

    /**
     * 获取科室分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取科室分页列表")
    @PostMapping("/getDepartmentPage")
    @Permission("department:page")
    public ApiResult<DepartmentVo> getDepartmentPage(@Valid @RequestBody DepartmentQuery query) {
        log.info("获取科室分页列表：{}", query);
        Paging<DepartmentVo> paging = departmentService.getDepartmentPage(query);
        return ApiResult.success(paging);
    }

}
