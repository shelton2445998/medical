package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppHospitalQuery;
import com.fourth.medical.medical.service.HospitalService;
import com.fourth.medical.medical.vo.AppHospitalVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * App医院 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App医院")
@RequestMapping("/hospital")
public class AppHospitalController {

    @Autowired
    private HospitalService hospitalService;

    /**
     * 获取医院详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取医院详情")
    @GetMapping("/detail/{id}")
    public ApiResult<AppHospitalVo> getHospitalDetail(@PathVariable Long id) {
        log.info("获取医院详情：{}", id);
        AppHospitalVo appHospitalVo = hospitalService.getAppHospitalById(id);
        return ApiResult.success(appHospitalVo);
    }

    /**
     * 获取医院列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取医院列表")
    @GetMapping("/list")
    public ApiResult<AppHospitalVo> getHospitalList(@Valid AppHospitalQuery query) {
        log.info("获取医院列表：{}", query);
        Paging<AppHospitalVo> paging = hospitalService.getAppHospitalPage(query);
        return ApiResult.success(paging);
    }

    /**
     * 获取推荐医院
     *
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取推荐医院")
    @GetMapping("/recommend")
    public ApiResult<List<AppHospitalVo>> getRecommendHospitals() {
        log.info("获取推荐医院");
        // 这里假设Service中已经有获取推荐医院的方法
        // 如果没有，则需要在Service中添加该方法
        List<AppHospitalVo> recommendHospitals = hospitalService.getRecommendHospitals();
        return ApiResult.success(recommendHospitals);
    }

}
