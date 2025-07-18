package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppSetmealQuery;
import com.fourth.medical.medical.service.SetmealService;
import com.fourth.medical.medical.vo.AppSetmealVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * App体检套餐 控制器
 * 
 * 功能说明：
 * 本控制器为移动端用户提供体检套餐的查询服务，支持通过ID获取套餐详情、分页获取套餐列表、
 * 以及获取推荐套餐。主要用于App端体检套餐展示、套餐选择等场景。
 * 
 * 主要功能：
 * 1. 获取套餐详情 - 通过套餐ID查询详细信息
 * 2. 分页获取套餐列表 - 支持多条件分页查询
 * 3. 获取推荐套餐 - 获取系统推荐的体检套餐
 * 
 * 业务流程：
 * 1. 用户在App端发起套餐查询请求
 * 2. 控制器接收请求参数，调用服务层进行业务处理
 * 3. 服务层返回套餐信息，控制器封装为统一响应格式
 * 4. 返回结果给前端展示
 * 
 * 权限控制：
 * - 仅限已登录用户访问（通常通过Token校验）
 * - 查询操作无需特殊权限
 * 
 * 数据安全：
 * - 仅返回对用户可见的套餐信息，敏感数据不对外暴露
 * - 日志记录所有查询操作，便于审计
 * 
 * 扩展点：
 * - 可扩展套餐标签、适用人群、价格等信息
 * - 支持多条件复合筛选
 * 
 * @author fourth
 * @since 2025-07-09
 * @version 1.0
 */
@Slf4j
@RestController
@Tag(name = "App体检套餐")
@RequestMapping("/package")
public class AppSetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 获取体检套餐详情
     * 
     * 功能说明：
     * 根据套餐ID查询详细信息，返回套餐的基本资料、包含项目、价格等。
     * 主要用于App端体检套餐详情页展示。
     * 
     * @param id 套餐ID
     * @return ApiResult<AppSetmealVo> 套餐详情信息
     */
    @Operation(summary = "获取体检套餐详情")
    @GetMapping("/detail/{id}")
    public ApiResult<AppSetmealVo> getSetmealDetail(@PathVariable Long id) {
        log.info("获取体检套餐详情：{}", id);
        AppSetmealVo appSetmealVo = setmealService.getAppSetmealById(id);
        return ApiResult.success(appSetmealVo);
    }

    /**
     * 获取体检套餐列表
     * 
     * 功能说明：
     * 支持多条件分页查询套餐信息，便于用户浏览和筛选体检套餐。
     * 
     * @param query 查询条件
     * @return ApiResult<AppSetmealVo> 分页套餐列表
     */
    @Operation(summary = "获取体检套餐列表")
    @GetMapping("/list")
    public ApiResult<AppSetmealVo> getSetmealList(@Valid AppSetmealQuery query) {
        log.info("获取体检套餐列表：{}", query);
        Paging<AppSetmealVo> paging = setmealService.getAppSetmealPage(query);
        return ApiResult.success(paging);
    }

    /**
     * 获取推荐体检套餐
     * 
     * 功能说明：
     * 获取系统推荐的体检套餐，便于用户快速选择适合的体检方案。
     * 
     * @return ApiResult<List<AppSetmealVo>> 推荐套餐列表
     */
    @Operation(summary = "获取推荐体检套餐")
    @GetMapping("/recommend")
    public ApiResult<List<AppSetmealVo>> getRecommendSetmeals() {
        log.info("获取推荐体检套餐");
        List<AppSetmealVo> recommendSetmeals = setmealService.getRecommendSetmeals();
        return ApiResult.success(recommendSetmeals);
    }

}
