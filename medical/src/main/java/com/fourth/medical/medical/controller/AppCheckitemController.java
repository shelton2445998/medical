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
 * 功能说明：
 * 本控制器为移动端用户提供体检检查项的信息查询服务，支持通过ID获取检查项详情、分页获取检查项列表、
 * 以及根据检查项ID批量获取部门信息。主要用于App端体检套餐选择、预约流程等场景。
 * 
 * 主要功能：
 * 1. 获取检查项详情 - 通过检查项ID查询详细信息
 * 2. 分页获取检查项列表 - 支持多条件分页查询
 * 3. 根据检查项ID列表获取部门信息 - 支持批量查询
 * 
 * 业务流程：
 * 1. 用户在App端发起检查项信息查询请求
 * 2. 控制器接收请求参数，调用服务层进行业务处理
 * 3. 服务层返回检查项信息，控制器封装为统一响应格式
 * 4. 返回结果给前端展示
 * 
 * 权限控制：
 * - 仅限已登录用户访问（通常通过Token校验）
 * - 查询操作无需特殊权限
 * 
 * 数据安全：
 * - 仅返回对用户可见的检查项信息，敏感数据不对外暴露
 * - 日志记录所有查询操作，便于审计
 * 
 * 扩展点：
 * - 可扩展检查项标签、适用人群、价格等信息
 * - 支持多条件复合筛选
 * 
 * @author fourth
 * @since 2025-07-09
 * @version 1.0
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
     * 功能说明：
     * 根据检查项ID查询详细信息，返回检查项的基本资料、所属科室、项目说明等。
     * 主要用于App端体检套餐详情页展示。
     * 
     * @param id 检查项ID
     * @return ApiResult<AppCheckitemVo> 检查项详情信息
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
     * 功能说明：
     * 支持多条件分页查询检查项信息，便于用户浏览和筛选体检项目。
     * 
     * @param query 查询条件
     * @return ApiResult<AppCheckitemVo> 分页检查项列表
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
     * 功能说明：
     * 支持通过检查项ID列表批量查询对应的部门信息，便于体检流程中自动分配科室。
     * 
     * @param checkitemIds 检查项ID列表，逗号分隔
     * @return ApiResult<Object> 部门信息
     */
    @Operation(summary = "根据检查项ID列表获取部门信息")
    @GetMapping("/getDepartmentByCheckitemIds")
    public ApiResult<Object> getDepartmentByCheckitemIds(@RequestParam String checkitemIds) {
        log.info("根据检查项ID列表获取部门信息：{}", checkitemIds);
        Object result = checkitemService.getDepartmentByCheckitemIds(checkitemIds);
        return ApiResult.success(result);
    }

}
