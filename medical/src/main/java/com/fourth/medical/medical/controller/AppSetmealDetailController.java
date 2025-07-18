package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppSetmealDetailQuery;
import com.fourth.medical.medical.service.SetmealDetailService;
import com.fourth.medical.medical.vo.AppSetmealDetailVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App体检套餐明细 控制器
 * 
 * 功能说明：
 * 本控制器为移动端用户提供体检套餐明细的查询服务，支持通过ID获取套餐明细详情、分页获取套餐明细列表。
 * 主要用于App端体检套餐详情展示、套餐内容查看等场景。
 * 
 * 主要功能：
 * 1. 获取套餐明细详情 - 通过明细ID查询详细信息
 * 2. 分页获取套餐明细列表 - 支持多条件分页查询
 * 
 * 业务流程：
 * 1. 用户在App端发起套餐明细查询请求
 * 2. 控制器接收请求参数，调用服务层进行业务处理
 * 3. 服务层返回套餐明细信息，控制器封装为统一响应格式
 * 4. 返回结果给前端展示
 * 
 * 权限控制：
 * - 仅限已登录用户访问（通常通过Token校验）
 * - 查询操作无需特殊权限
 * 
 * 数据安全：
 * - 仅返回对用户可见的套餐明细信息，敏感数据不对外暴露
 * - 日志记录所有查询操作，便于审计
 * 
 * 扩展点：
 * - 可扩展套餐明细标签、适用人群、价格等信息
 * - 支持多条件复合筛选
 * 
 * @author fourth
 * @since 2025-07-09
 * @version 1.0
 */
@Slf4j
@RestController
@Tag(name = "App体检套餐明细")
@RequestMapping("/app/setmealDetail")
public class AppSetmealDetailController {

    @Autowired
    private SetmealDetailService setmealDetailService;

    /**
     * 获取App体检套餐明细详情
     * 
     * 功能说明：
     * 根据套餐明细ID查询详细信息，返回套餐明细的基本资料、所属套餐、项目说明等。
     * 主要用于App端体检套餐详情页展示。
     * 
     * @param id 套餐明细ID
     * @return ApiResult<AppSetmealDetailVo> 套餐明细详情信息
     */
    @Operation(summary = "获取App体检套餐明细详情")
    @PostMapping("/getAppSetmealDetail/{id}")
    public ApiResult<AppSetmealDetailVo> getAppSetmealDetail(@PathVariable Long id) {
        log.info("获取App体检套餐明细详情：{}", id);
        AppSetmealDetailVo appSetmealDetailVo = setmealDetailService.getAppSetmealDetailById(id);
        return ApiResult.success(appSetmealDetailVo);
    }

    /**
     * 获取App体检套餐明细分页列表
     * 
     * 功能说明：
     * 支持多条件分页查询套餐明细信息，便于用户浏览和筛选套餐内容。
     * 
     * @param query 查询条件
     * @return ApiResult<AppSetmealDetailVo> 分页套餐明细列表
     */
    @Operation(summary = "获取App体检套餐明细分页列表")
    @PostMapping("/getAppSetmealDetailPage")
    public ApiResult<AppSetmealDetailVo> getAppSetmealDetailPage(@Valid @RequestBody AppSetmealDetailQuery query) {
        log.info("获取App体检套餐明细分页列表：{}", query);
        Paging<AppSetmealDetailVo> paging = setmealDetailService.getAppSetmealDetailPage(query);
        return ApiResult.success(paging);
    }

}
