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

    /**
     * 根据医院ID和部门ID查找医生
     *
     * @param hospitalId 医院ID
     * @param departmentId 部门ID
     * @return 医生列表
     * @throws Exception
     */
    @Operation(summary = "根据医院ID和部门ID查找医生")
    @GetMapping("/getDoctorByHospitalAndDepartment")
    public ApiResult<Object> getDoctorByHospitalAndDepartment(@RequestParam String hospitalId, @RequestParam String departmentId) {
        log.info("根据医院ID和部门ID查找医生：hospitalId={}, departmentId={}", hospitalId, departmentId);
        
        try {
            Long hospitalIdLong = Long.parseLong(hospitalId);
            Long departmentIdLong = Long.parseLong(departmentId);
            
            log.info("转换后的参数：hospitalId={}, departmentId={}", hospitalIdLong, departmentIdLong);
            
            Object result = doctorService.getDoctorByHospitalAndDepartment(hospitalIdLong, departmentIdLong);
            return ApiResult.success(result);
        } catch (NumberFormatException e) {
            log.error("参数转换失败：hospitalId={}, departmentId={}", hospitalId, departmentId, e);
            return ApiResult.fail("参数格式错误");
        }
    }

}
