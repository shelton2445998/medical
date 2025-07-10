package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppDoctorQuery;
import com.fourth.medical.medical.service.DoctorService;
import com.fourth.medical.medical.vo.AppDoctorVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App医生 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App医生")
@RequestMapping("/app/doctor")
public class AppDoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * 获取App医生详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App医生详情")
    @PostMapping("/getAppDoctor/{id}")
    public ApiResult<AppDoctorVo> getAppDoctor(@PathVariable Long id) {
        log.info("获取App医生详情：{}", id);
        AppDoctorVo appDoctorVo = doctorService.getAppDoctorById(id);
        return ApiResult.success(appDoctorVo);
    }

    /**
     * 获取App医生分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App医生分页列表")
    @PostMapping("/getAppDoctorPage")
    public ApiResult<AppDoctorVo> getAppDoctorPage(@Valid @RequestBody AppDoctorQuery query) {
        log.info("获取App医生分页列表：{}", query);
        Paging<AppDoctorVo> paging = doctorService.getAppDoctorPage(query);
        return ApiResult.success(paging);
    }

}
