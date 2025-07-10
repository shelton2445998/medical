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
 * @author fourth
 * @since 2025-07-09
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
     * @param id
     * @return
     * @throws Exception
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
     * @param query
     * @return
     * @throws Exception
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
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取推荐体检套餐")
    @GetMapping("/recommend")
    public ApiResult<List<AppSetmealVo>> getRecommendSetmeals() {
        log.info("获取推荐体检套餐");
        List<AppSetmealVo> recommendSetmeals = setmealService.getRecommendSetmeals();
        return ApiResult.success(recommendSetmeals);
    }

}
