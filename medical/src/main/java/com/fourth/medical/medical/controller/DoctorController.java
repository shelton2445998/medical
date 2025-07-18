package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.DoctorDto;
import com.fourth.medical.medical.dto.DoctorUpdatePasswordDto;
import com.fourth.medical.medical.query.DoctorQuery;
import com.fourth.medical.medical.service.DoctorService;
import com.fourth.medical.medical.vo.DoctorVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 医生信息管理控制器
 * 
 * 功能说明：
 * 本控制器是医疗体检系统中医生信息管理的核心控制器，负责处理医生信息的全生命周期管理，
 * 包括医生的基本信息维护、专业资质管理、科室分配、排班调度等功能。该控制器主要服务于
 * 管理后台，为医院管理人员提供完整的医生信息管理功能。
 * 
 * 主要功能：
 * 1. 医生基础信息管理
 *    - 新增医生档案
 *    - 修改医生信息
 *    - 删除医生档案
 *    - 查询医生详情
 *    - 医生列表分页查询
 * 
 * 2. 医生专业信息管理
 *    - 医生专业资质信息
 *    - 医生执业证书管理
 *    - 医生专业技能记录
 *    - 医生学历背景信息
 * 
 * 3. 医生工作信息管理
 *    - 医生科室分配
 *    - 医生职位级别设置
 *    - 医生工作状态管理
 *    - 医生服务范围定义
 * 
 * 4. 医生状态管理
 *    - 医生在职状态控制
 *    - 医生可预约状态管理
 *    - 医生工作时间设置
 *    - 医生服务状态监控
 * 
 * 业务特点：
 * - 严格的权限控制，确保只有授权人员能够管理医生信息
 * - 完整的操作日志记录，便于审计和问题追踪
 * - 数据完整性验证，确保医生信息的准确性
 * - 支持复杂查询条件，提供灵活的数据检索功能
 * - 分页查询优化，提高大数据量处理性能
 * 
 * 权限体系：
 * - doctor:add - 新增医生权限
 * - doctor:update - 修改医生权限
 * - doctor:delete - 删除医生权限
 * - doctor:info - 查看医生信息权限
 * - doctor:page - 医生列表查询权限
 * 
 * 数据安全：
 * - 医生个人信息保护
 * - 敏感数据访问控制
 * - 操作权限验证
 * - 数据完整性校验
 * 
 * 关联模块：
 * - 科室管理：医生所属科室信息
 * - 排班管理：医生工作时间安排
 * - 预约管理：医生可预约时间段
 * - 体检管理：医生参与的体检项目
 * 
 * @author fourth
 * @since 2025-07-09
 * @version 1.0
 */
@Slf4j  // 自动生成日志对象log
@RestController  // 标识这是一个REST控制器，返回JSON格式数据
@Tag(name = "医生")  // Swagger文档标签，用于API分组
@RequestMapping("/admin/doctor")  // 设置控制器的基础请求路径
public class DoctorController {

    /**
     * 医生服务接口
     * 提供医生信息的业务逻辑处理，包括增删改查等核心功能
     */
    @Autowired
    private DoctorService doctorService;

    /**
     * 添加医生信息
     * 
     * 功能说明：
     * 新增医生档案信息，包括医生的基本信息、专业资质、科室分配等。
     * 该接口用于医院管理人员向系统中添加新的医生记录。
     * 
     * 业务流程：
     * 1. 接收前端传递的医生信息数据
     * 2. 进行数据验证和格式检查
     * 3. 检查医生工号或身份证是否重复
     * 4. 验证所属科室是否存在
     * 5. 保存医生信息到数据库
     * 6. 记录操作日志
     * 7. 返回操作结果
     * 
     * 数据验证：
     * - 医生姓名不能为空
     * - 医生工号必须唯一
     * - 身份证号码格式验证
     * - 联系电话格式验证
     * - 所属科室必须存在
     * 
     * 权限要求：
     * - 需要doctor:add权限
     * - 只有管理员和人事管理员可以操作
     * 
     * @param dto 医生信息数据传输对象，包含医生的所有基本信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、数据库操作异常等
     */
    @Log(type = SysLogType.ADD)  // 记录操作日志
    @Operation(summary = "添加医生")  // Swagger接口描述
    @PostMapping("/addDoctor")  // HTTP POST请求映射
    @Permission("doctor:add")  // 权限验证
    public ApiResult addDoctor(@Valid @RequestBody DoctorDto dto) {
        log.info("添加医生：{}", dto);
        boolean flag = doctorService.addDoctor(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改医生信息
     * 
     * 功能说明：
     * 更新已存在的医生档案信息，支持修改医生的基本信息、专业资质、
     * 科室分配、工作状态等各项内容。
     * 
     * 业务流程：
     * 1. 接收前端传递的医生更新数据
     * 2. 验证医生ID的有效性
     * 3. 检查医生是否存在
     * 4. 进行数据格式验证
     * 5. 检查工号是否与其他医生重复
     * 6. 验证科室变更的合法性
     * 7. 更新医生信息
     * 8. 记录操作日志
     * 9. 返回操作结果
     * 
     * 数据验证：
     * - 医生ID必须存在
     * - 修改后的工号不能与其他医生重复
     * - 新的科室分配必须有效
     * - 联系信息格式验证
     * 
     * 特殊业务规则：
     * - 如果医生有未完成的预约，不能修改科室
     * - 如果医生正在值班，不能修改工作状态
     * - 某些关键信息可能需要上级审批
     * 
     * 权限要求：
     * - 需要doctor:update权限
     * - 只有管理员和人事管理员可以操作
     * 
     * @param dto 医生信息数据传输对象，包含要更新的医生信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
     */
    @Log(type = SysLogType.UPDATE)  // 记录操作日志
    @Operation(summary = "修改医生")  // Swagger接口描述
    @PostMapping("/updateDoctor")  // HTTP POST请求映射
    @Permission("doctor:update")  // 权限验证
    public ApiResult updateDoctor(@Valid @RequestBody DoctorDto dto) {
        log.info("修改医生：{}", dto);
        boolean flag = doctorService.updateDoctor(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除医生信息
     * 
     * 功能说明：
     * 删除指定的医生档案信息。出于数据安全考虑，通常采用逻辑删除方式，
     * 即将医生状态标记为已删除，而不是物理删除数据记录。
     * 
     * 业务流程：
     * 1. 接收要删除的医生ID
     * 2. 验证医生ID的有效性
     * 3. 检查医生是否存在
     * 4. 验证是否可以删除（业务规则检查）
     * 5. 检查关联数据（预约、排班等）
     * 6. 执行删除操作（逻辑删除）
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * 删除前检查：
     * - 医生是否有未完成的预约
     * - 医生是否有未来的排班安排
     * - 医生是否有未完成的体检任务
     * - 医生是否有关联的报告审核任务
     * 
     * 业务规则：
     * - 有活跃业务的医生不能删除
     * - 删除操作需要记录详细日志
     * - 删除后的医生信息需要保留一定时间用于审计
     * 
     * 权限要求：
     * - 需要doctor:delete权限
     * - 通常只有高级管理员可以执行删除操作
     * 
     * @param id 医生的唯一标识ID
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据不存在异常、业务规则异常等
     */
    @Log(type = SysLogType.DELETE)  // 记录操作日志
    @Operation(summary = "删除医生")  // Swagger接口描述
    @PostMapping("/deleteDoctor/{id}")  // HTTP POST请求映射，路径变量
    @Permission("doctor:delete")  // 权限验证
    public ApiResult deleteDoctor(@PathVariable Long id) {
        log.info("删除医生：{}", id);
        boolean flag = doctorService.deleteDoctor(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取医生详细信息
     * 
     * 功能说明：
     * 根据医生ID查询并返回医生的详细信息，包括基本信息、专业资质、
     * 科室归属、工作状态等完整的医生档案数据。
     * 
     * 业务流程：
     * 1. 接收医生ID参数
     * 2. 验证ID的有效性
     * 3. 查询医生基本信息
     * 4. 查询医生专业资质信息
     * 5. 查询医生科室关联信息
     * 6. 查询医生工作状态信息
     * 7. 组装完整的医生信息对象
     * 8. 返回查询结果
     * 
     * 返回数据内容：
     * - 医生基本信息（姓名、工号、身份证等）
     * - 专业资质信息（职称、执业证书等）
     * - 科室归属信息（所属科室、职位等）
     * - 工作状态信息（在职状态、可预约状态等）
     * - 联系信息（电话、邮箱、地址等）
     * 
     * 数据安全：
     * - 敏感信息根据权限决定是否返回
     * - 个人隐私信息需要特殊权限才能查看
     * - 删除或停用的医生信息有特殊标识
     * 
     * 权限要求：
     * - 需要doctor:info权限
     * - 不同权限级别可以查看不同详细程度的信息
     * 
     * @param id 医生的唯一标识ID
     * @return ApiResult<DoctorVo> 返回包含医生详细信息的响应对象
     * @throws Exception 可能抛出的异常包括数据不存在异常、权限异常等
     */
    @Operation(summary = "获取医生详情")  // Swagger接口描述
    @PostMapping("/getDoctor/{id}")  // HTTP POST请求映射，路径变量
    @Permission("doctor:info")  // 权限验证
    public ApiResult<DoctorVo> getDoctor(@PathVariable Long id) {
        log.info("获取医生详情：{}", id);
        DoctorVo doctorVo = doctorService.getDoctorById(id);
        return ApiResult.success(doctorVo);
    }

    /**
     * 获取医生分页列表
     * 
     * 功能说明：
     * 根据查询条件分页查询医生列表，支持多种查询条件组合，
     * 为管理界面提供灵活的医生信息检索功能。
     * 
     * 业务流程：
     * 1. 接收查询条件和分页参数
     * 2. 验证查询参数的有效性
     * 3. 构建查询条件
     * 4. 执行分页查询
     * 5. 处理查询结果
     * 6. 计算分页信息
     * 7. 返回分页结果
     * 
     * 支持的查询条件：
     * - 医生姓名模糊查询
     * - 医生工号精确查询
     * - 科室ID查询
     * - 职称等级查询
     * - 工作状态查询
     * - 创建时间范围查询
     * 
     * 分页参数：
     * - current：当前页码（默认为1）
     * - size：每页显示数量（默认为10）
     * - 支持排序字段和排序方向
     * 
     * 返回数据：
     * - records：当前页的医生记录列表
     * - total：符合条件的总记录数
     * - current：当前页码
     * - size：每页显示数量
     * - pages：总页数
     * 
     * 性能优化：
     * - 使用索引优化查询性能
     * - 大数据量时启用查询缓存
     * - 避免查询过多的字段
     * 
     * 权限要求：
     * - 需要doctor:page权限
     * - 不同权限级别可能看到不同的数据范围
     * 
     * @param query 查询条件对象，包含各种查询参数和分页信息
     * @return ApiResult<DoctorVo> 返回包含医生分页数据的响应对象
     * @throws Exception 可能抛出的异常包括查询参数异常、数据库异常等
     */
    @Operation(summary = "获取医生分页列表")  // Swagger接口描述
    @PostMapping("/getDoctorPage")  // HTTP POST请求映射
    @Permission("doctor:page")  // 权限验证
    public ApiResult<DoctorVo> getDoctorPage(@Valid @RequestBody DoctorQuery query) {
        log.info("获取医生分页列表：{}", query);
        Paging<DoctorVo> paging = doctorService.getDoctorPage(query);
        return ApiResult.success(paging);
    }

    /**
     * 修改医生密码
     * 
     * 功能说明：
     * 允许医生修改自己的登录密码，需要提供原密码和新密码。
     * 
     * 业务流程：
     * 1. 验证原密码是否正确
     * 2. 检查新密码的合法性
     * 3. 加密新密码
     * 4. 更新密码
     * 5. 记录操作日志
     * 
     * 安全要求：
     * - 必须验证原密码
     * - 新密码需要符合复杂度要求
     * - 密码加密存储
     * 
     * @param dto 密码修改信息
     * @return 操作结果
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public ApiResult updatePassword(@Valid @RequestBody DoctorUpdatePasswordDto dto) {
        log.info("医生修改密码");
        boolean flag = doctorService.updatePassword(dto);
        return ApiResult.result(flag);
    }

}
