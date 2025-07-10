package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppDepartmentQuery;
import com.fourth.medical.medical.service.DepartmentService;
import com.fourth.medical.medical.vo.AppDepartmentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App科室 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App科室")
@RequestMapping("/app/department")
public class AppDepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 获取App科室详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App科室详情")
    @PostMapping("/getAppDepartment/{id}")
    public ApiResult<AppDepartmentVo> getAppDepartment(@PathVariable Long id) {
        log.info("获取App科室详情：{}", id);
        AppDepartmentVo appDepartmentVo = departmentService.getAppDepartmentById(id);
        return ApiResult.success(appDepartmentVo);
    }

    /**
     * 获取App科室分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App科室分页列表")
    @PostMapping("/getAppDepartmentPage")
    public ApiResult<AppDepartmentVo> getAppDepartmentPage(@Valid @RequestBody AppDepartmentQuery query) {
        log.info("获取App科室分页列表：{}", query);
        Paging<AppDepartmentVo> paging = departmentService.getAppDepartmentPage(query);
        return ApiResult.success(paging);
    }

}
