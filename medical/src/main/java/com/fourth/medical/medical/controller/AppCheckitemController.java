package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppCheckitemQuery;
import com.fourth.medical.medical.service.CheckitemService;
import com.fourth.medical.medical.vo.AppCheckitemVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App检查项 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App检查项")
@RequestMapping("/app/checkitem")
public class AppCheckitemController {

    @Autowired
    private CheckitemService checkitemService;

    /**
     * 获取App检查项详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App检查项详情")
    @PostMapping("/getAppCheckitem/{id}")
    public ApiResult<AppCheckitemVo> getAppCheckitem(@PathVariable Long id) {
        log.info("获取App检查项详情：{}", id);
        AppCheckitemVo appCheckitemVo = checkitemService.getAppCheckitemById(id);
        return ApiResult.success(appCheckitemVo);
    }

    /**
     * 获取App检查项分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App检查项分页列表")
    @PostMapping("/getAppCheckitemPage")
    public ApiResult<AppCheckitemVo> getAppCheckitemPage(@Valid @RequestBody AppCheckitemQuery query) {
        log.info("获取App检查项分页列表：{}", query);
        log.info("医院ID: {}, 科室ID: {}", query.getHospitalId(), query.getDepartmentId());
        Paging<AppCheckitemVo> paging = checkitemService.getAppCheckitemPage(query);
        log.info("查询结果数量: {}", paging.getList().size());
        return ApiResult.success(paging);
    }

    /**
     * 根据检查项ID列表获取部门信息
     *
     * @param checkitemIds 检查项ID列表，逗号分隔
     * @return 部门信息
     * @throws Exception
     */
    @Operation(summary = "根据检查项ID列表获取部门信息")
    @GetMapping("/getDepartmentByCheckitemIds")
    public ApiResult<Object> getDepartmentByCheckitemIds(@RequestParam String checkitemIds) {
        log.info("根据检查项ID列表获取部门信息：{}", checkitemIds);
        Object result = checkitemService.getDepartmentByCheckitemIds(checkitemIds);
        return ApiResult.success(result);
    }

}
