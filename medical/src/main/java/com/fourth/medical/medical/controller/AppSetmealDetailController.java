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
 * @author fourth
 * @since 2025-07-09
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
     * @param id
     * @return
     * @throws Exception
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
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App体检套餐明细分页列表")
    @PostMapping("/getAppSetmealDetailPage")
    public ApiResult<AppSetmealDetailVo> getAppSetmealDetailPage(@Valid @RequestBody AppSetmealDetailQuery query) {
        log.info("获取App体检套餐明细分页列表：{}", query);
        Paging<AppSetmealDetailVo> paging = setmealDetailService.getAppSetmealDetailPage(query);
        return ApiResult.success(paging);
    }

}
