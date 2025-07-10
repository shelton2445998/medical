package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppCheckitemDetailQuery;
import com.fourth.medical.medical.service.CheckitemDetailService;
import com.fourth.medical.medical.vo.AppCheckitemDetailVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App检查项明细 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App检查项明细")
@RequestMapping("/app/checkitemDetail")
public class AppCheckitemDetailController {

    @Autowired
    private CheckitemDetailService checkitemDetailService;

    /**
     * 获取App检查项明细详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App检查项明细详情")
    @PostMapping("/getAppCheckitemDetail/{id}")
    public ApiResult<AppCheckitemDetailVo> getAppCheckitemDetail(@PathVariable Long id) {
        log.info("获取App检查项明细详情：{}", id);
        AppCheckitemDetailVo appCheckitemDetailVo = checkitemDetailService.getAppCheckitemDetailById(id);
        return ApiResult.success(appCheckitemDetailVo);
    }

    /**
     * 获取App检查项明细分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App检查项明细分页列表")
    @PostMapping("/getAppCheckitemDetailPage")
    public ApiResult<AppCheckitemDetailVo> getAppCheckitemDetailPage(@Valid @RequestBody AppCheckitemDetailQuery query) {
        log.info("获取App检查项明细分页列表：{}", query);
        Paging<AppCheckitemDetailVo> paging = checkitemDetailService.getAppCheckitemDetailPage(query);
        return ApiResult.success(paging);
    }

}
