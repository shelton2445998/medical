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
 * 功能说明：
 * 本控制器为移动端用户提供医生信息的查询服务，支持通过ID获取医生详情、分页获取医生列表、
 * 以及根据医院和科室筛选医生。主要用于App端医生展示、预约挂号等场景。
 * 
 * 主要功能：
 * 1. 获取医生详情 - 通过医生ID查询医生详细信息
 * 2. 分页获取医生列表 - 支持多条件分页查询医生
 * 3. 按医院和科室筛选医生 - 便于用户快速定位目标医生
 * 
 * 业务流程：
 * 1. 用户在App端发起医生信息查询请求
 * 2. 控制器接收请求参数，调用服务层进行业务处理
 * 3. 服务层返回医生信息，控制器封装为统一响应格式
 * 4. 返回结果给前端展示
 * 
 * 权限控制：
 * - 仅限已登录用户访问（通常通过Token校验）
 * - 查询操作无需特殊权限
 * 
 * 数据安全：
 * - 仅返回对用户可见的医生信息，敏感数据不对外暴露
 * - 日志记录所有查询操作，便于审计
 * 
 * 扩展点：
 * - 可扩展医生标签、评分、预约状态等信息
 * - 支持多条件复合筛选
 * 
 * @author fourth
 * @since 2025-07-09
 * @version 1.0
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
     * 功能说明：
     * 根据医生ID查询医生详细信息，返回医生的基本资料、专业信息、所属科室等。
     * 主要用于App端医生详情页展示。
     * 
     * @param id 医生ID
     * @return ApiResult<AppDoctorVo> 医生详情信息
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
     * 功能说明：
     * 支持多条件分页查询医生信息，便于用户浏览和筛选医生。
     * 
     * @param query 查询条件
     * @return ApiResult<AppDoctorVo> 分页医生列表
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
     * 功能说明：
     * 支持通过医院ID和科室ID联合筛选医生，便于用户在指定医院和科室下查找目标医生。
     * 
     * @param hospitalId 医院ID
     * @param departmentId 部门ID
     * @return ApiResult<Object> 医生列表
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
