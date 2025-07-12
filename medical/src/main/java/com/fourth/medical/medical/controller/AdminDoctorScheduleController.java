package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.DoctorScheduleDto;
import com.fourth.medical.medical.query.DoctorScheduleQuery;
import com.fourth.medical.medical.service.DoctorScheduleService;
import com.fourth.medical.medical.vo.DoctorScheduleVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 医生排班 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "医生排班")
@RequestMapping("/admin/doctorSchedule")
public class AdminDoctorScheduleController {

    @Autowired
    private DoctorScheduleService doctorScheduleService;

    /**
     * 添加医生排班
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加医生排班")
    @PostMapping("/addDoctorSchedule")
    @Permission("doctor:schedule:add")
    public ApiResult addDoctorSchedule(@Valid @RequestBody DoctorScheduleDto dto) {
        log.info("添加医生排班：{}", dto);
        boolean flag = doctorScheduleService.addDoctorSchedule(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改医生排班
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改医生排班")
    @PostMapping("/updateDoctorSchedule")
    @Permission("doctor:schedule:update")
    public ApiResult updateDoctorSchedule(@Valid @RequestBody DoctorScheduleDto dto) {
        log.info("修改医生排班：{}", dto);
        boolean flag = doctorScheduleService.updateDoctorSchedule(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除医生排班
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除医生排班")
    @PostMapping("/deleteDoctorSchedule/{id}")
    @Permission("doctor:schedule:delete")
    public ApiResult deleteDoctorSchedule(@PathVariable Long id) {
        log.info("删除医生排班：{}", id);
        boolean flag = doctorScheduleService.deleteDoctorSchedule(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取医生排班详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取医生排班详情")
    @PostMapping("/getDoctorSchedule/{id}")
    @Permission("doctor:schedule:info")
    public ApiResult<DoctorScheduleVo> getDoctorSchedule(@PathVariable Long id) {
        log.info("获取医生排班详情：{}", id);
        DoctorScheduleVo doctorScheduleVo = doctorScheduleService.getDoctorScheduleById(id);
        return ApiResult.success(doctorScheduleVo);
    }

    /**
     * 获取医生排班分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取医生排班分页列表")
    @PostMapping("/getDoctorSchedulePage")
    @Permission("doctor:schedule:page")
    public ApiResult<DoctorScheduleVo> getDoctorSchedulePage(@Valid @RequestBody DoctorScheduleQuery query) {
        log.info("获取医生排班分页列表：{}", query);
        Paging<DoctorScheduleVo> paging = doctorScheduleService.getDoctorSchedulePage(query);
        return ApiResult.success(paging);
    }

}
