package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppDoctorScheduleQuery;
import com.fourth.medical.medical.service.DoctorScheduleService;
import com.fourth.medical.medical.vo.AppDoctorScheduleVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App医生排班 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App医生排班")
@RequestMapping("/app/doctorSchedule")
public class AppDoctorScheduleController {

    @Autowired
    private DoctorScheduleService doctorScheduleService;

    /**
     * 获取App医生排班详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App医生排班详情")
    @PostMapping("/getAppDoctorSchedule/{id}")
    public ApiResult<AppDoctorScheduleVo> getAppDoctorSchedule(@PathVariable Long id) {
        log.info("获取App医生排班详情：{}", id);
        AppDoctorScheduleVo appDoctorScheduleVo = doctorScheduleService.getAppDoctorScheduleById(id);
        return ApiResult.success(appDoctorScheduleVo);
    }

    /**
     * 获取App医生排班分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App医生排班分页列表")
    @PostMapping("/getAppDoctorSchedulePage")
    public ApiResult<AppDoctorScheduleVo> getAppDoctorSchedulePage(@Valid @RequestBody AppDoctorScheduleQuery query) {
        log.info("获取App医生排班分页列表：{}", query);
        Paging<AppDoctorScheduleVo> paging = doctorScheduleService.getAppDoctorSchedulePage(query);
        return ApiResult.success(paging);
    }

}
