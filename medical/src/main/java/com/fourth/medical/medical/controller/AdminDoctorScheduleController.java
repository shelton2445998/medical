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
 * 功能说明：
 * 本控制器是医疗体检系统中医生排班管理的核心控制器，负责处理医生排班的全生命周期管理，
 * 包括排班的创建、修改、删除、查询等功能。该控制器主要服务于管理后台，为医院管理人员
 * 提供完整的医生排班管理功能。
 * 
 * 主要功能：
 * 1. 医生排班基础管理
 *    - 新增医生排班
 *    - 修改排班信息
 *    - 删除排班档案
 *    - 查询排班详情
 *    - 排班列表分页查询
 * 
 * 2. 排班时间管理
 *    - 排班日期设置
 *    - 工作时间段配置
 *    - 排班容量管理
 *    - 排班冲突检测
 * 
 * 3. 排班状态管理
 *    - 排班启用状态控制
 *    - 排班预约状态管理
 *    - 排班完成状态维护
 *    - 排班异常状态处理
 * 
 * 4. 排班权限管理
 *    - 排班创建权限控制
 *    - 排班修改权限验证
 *    - 排班删除权限检查
 *    - 排班查看权限管理
 * 
 * 业务特点：
 * - 严格的权限控制，确保排班信息的安全性
 * - 完整的操作日志记录，便于审计和问题追踪
 * - 数据完整性验证，确保排班信息的准确性
 * - 支持复杂查询条件，提供灵活的数据检索功能
 * - 分页查询优化，提高大数据量处理性能
 * 
 * 权限体系：
 * - doctor:schedule:add - 新增排班权限
 * - doctor:schedule:update - 修改排班权限
 * - doctor:schedule:delete - 删除排班权限
 * - doctor:schedule:info - 查看排班信息权限
 * - doctor:schedule:page - 排班列表查询权限
 * 
 * 数据安全：
 * - 排班信息访问控制
 * - 敏感数据权限验证
 * - 操作权限严格检查
 * - 数据完整性校验
 * 
 * 关联模块：
 * - 医生管理：排班关联的医生信息
 * - 科室管理：排班所属的科室信息
 * - 预约管理：排班的预约情况
 * - 医院管理：排班所属的医院信息
 * 
 * @author fourth
 * @since 2025-07-09
 * @version 1.0
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
     * 功能说明：
     * 新增医生排班信息，包括排班的基本信息、工作时间、容量等。
     * 该接口用于医院管理人员为医生安排工作时间。
     * 
     * 业务流程：
     * 1. 接收前端传递的排班信息数据
     * 2. 进行数据验证和格式检查
     * 3. 验证医生ID的有效性
     * 4. 检查排班时间是否冲突
     * 5. 验证排班容量的合理性
     * 6. 保存排班信息到数据库
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * 数据验证：
     * - 医生ID必须存在且有效
     * - 排班日期不能为过去的日期
     * - 排班时间不能与其他排班冲突
     * - 排班容量必须大于0
     * 
     * 权限要求：
     * - 需要doctor:schedule:add权限
     * - 只有管理员和排班管理员可以操作
     * 
     * @param dto 排班信息数据传输对象，包含排班的所有基本信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、数据库操作异常等
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
     * 功能说明：
     * 更新已存在的医生排班信息，支持修改排班的基本信息、工作时间、
     * 容量、状态等各项内容。
     * 
     * 业务流程：
     * 1. 接收前端传递的排班更新数据
     * 2. 验证排班ID的有效性
     * 3. 检查排班是否存在
     * 4. 进行数据格式验证
     * 5. 检查排班时间是否与其他排班冲突
     * 6. 验证修改权限和业务规则
     * 7. 更新排班信息
     * 8. 记录操作日志
     * 9. 返回操作结果
     * 
     * 数据验证：
     * - 排班ID必须存在
     * - 修改后的时间不能与其他排班冲突
     * - 排班容量变更需要验证合理性
     * - 医生信息变更需要验证权限
     * 
     * 特殊业务规则：
     * - 已有预约的排班修改需要特殊处理
     * - 排班状态变更需要检查业务影响
     * - 某些关键信息可能需要上级审批
     * 
     * 权限要求：
     * - 需要doctor:schedule:update权限
     * - 只有管理员和排班管理员可以操作
     * 
     * @param dto 排班信息数据传输对象，包含要更新的排班信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
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
     * 功能说明：
     * 删除指定的医生排班信息。出于数据安全考虑，通常采用逻辑删除方式，
     * 即将排班状态标记为已删除，而不是物理删除数据记录。
     * 
     * 业务流程：
     * 1. 接收要删除的排班ID
     * 2. 验证排班ID的有效性
     * 3. 检查排班是否存在
     * 4. 验证是否可以删除（业务规则检查）
     * 5. 检查关联数据（预约等）
     * 6. 执行删除操作（逻辑删除）
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * 数据验证：
     * - 排班ID必须存在
     * - 排班必须存在且未被删除
     * - 排班状态必须允许删除
     * 
     * 业务规则：
     * - 已有预约的排班不能删除
     * - 正在进行的排班不能删除
     * - 删除操作需要高级权限
     * 
     * 权限要求：
     * - 需要doctor:schedule:delete权限
     * - 只有管理员可以操作
     * 
     * @param id 排班ID
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
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
     * 功能说明：
     * 根据排班ID查询并返回医生排班的详细信息，包括排班基本信息、
     * 工作时间、容量、状态等。
     * 
     * 业务流程：
     * 1. 接收排班ID参数
     * 2. 验证排班ID的有效性
     * 3. 检查排班是否存在
     * 4. 验证访问权限
     * 5. 查询排班详细信息
     * 6. 返回排班数据
     * 
     * 数据验证：
     * - 排班ID必须存在
     * - 排班必须存在且未被删除
     * 
     * 权限要求：
     * - 需要doctor:schedule:info权限
     * - 只有管理员和授权用户可以查看
     * 
     * @param id 排班ID
     * @return ApiResult<DoctorScheduleVo> 返回排班详细信息
     * @throws Exception 可能抛出的异常包括数据验证异常、权限验证异常等
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
     * 功能说明：
     * 支持多条件分页查询医生排班信息，提供灵活的数据检索功能。
     * 
     * 业务流程：
     * 1. 接收查询条件参数
     * 2. 验证查询条件的有效性
     * 3. 构建查询条件
     * 4. 执行分页查询
     * 5. 返回分页结果
     * 
     * 查询条件：
     * - 医生ID：支持按医生筛选
     * - 科室ID：支持按科室筛选
     * - 排班日期：支持按日期筛选
     * - 排班状态：支持按状态筛选
     * - 时间范围：支持按时间范围筛选
     * 
     * 权限要求：
     * - 需要doctor:schedule:page权限
     * - 只有管理员和授权用户可以查询
     * 
     * @param query 查询条件对象，包含各种筛选条件
     * @return ApiResult<DoctorScheduleVo> 返回分页排班列表
     * @throws Exception 可能抛出的异常包括数据验证异常、权限验证异常等
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
