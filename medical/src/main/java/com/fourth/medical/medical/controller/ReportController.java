package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.ReportDto;
import com.fourth.medical.medical.query.ReportQuery;
import com.fourth.medical.medical.service.ReportService;
import com.fourth.medical.medical.vo.ReportVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 体检报告总 控制器
 * 
 * 功能说明：
 * 本控制器是医疗体检系统中体检报告管理的核心控制器，负责处理体检报告的全生命周期管理，
 * 包括报告的创建、修改、删除、查询等功能。体检报告是体检服务的最终成果，记录了用户的
 * 体检结果和医生的专业建议。
 * 
 * 主要功能：
 * 1. 体检报告基础管理
 *    - 新增体检报告
 *    - 修改报告信息
 *    - 删除报告档案
 *    - 查询报告详情
 *    - 报告列表分页查询
 * 
 * 2. 报告状态管理
 *    - 报告生成状态控制
 *    - 报告审核状态管理
 *    - 报告完成状态维护
 *    - 报告发布状态监控
 * 
 * 3. 报告内容管理
 *    - 报告基本信息维护
 *    - 报告检查项目关联
 *    - 报告医生信息管理
 *    - 报告结论和建议
 * 
 * 4. 报告权限管理
 *    - 报告访问权限控制
 *    - 报告修改权限验证
 *    - 报告删除权限检查
 *    - 报告分享权限管理
 * 
 * 业务特点：
 * - 严格的权限控制，确保报告信息的安全性
 * - 完整的操作日志记录，便于审计和问题追踪
 * - 数据完整性验证，确保报告信息的准确性
 * - 支持复杂查询条件，提供灵活的数据检索功能
 * - 分页查询优化，提高大数据量处理性能
 * 
 * 权限体系：
 * - report:add - 新增报告权限
 * - report:update - 修改报告权限
 * - report:delete - 删除报告权限
 * - report:info - 查看报告信息权限
 * - report:page - 报告列表查询权限
 * 
 * 数据安全：
 * - 报告信息访问控制
 * - 敏感数据权限验证
 * - 操作权限严格检查
 * - 数据完整性校验
 * 
 * 关联模块：
 * - 体检预约：报告关联的预约信息
 * - 检查项目：报告包含的检查内容
 * - 医生管理：报告负责的医生信息
 * - 用户管理：报告所属的用户信息
 * 
 * @author fourth
 * @since 2025-07-14
 * @version 1.0
 */
@Slf4j
@RestController
@Tag(name = "体检报告总")
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 添加体检报告总
     * 
     * 功能说明：
     * 新增体检报告信息，包括报告的基本信息、检查项目、医生建议等。
     * 该接口用于医院管理人员创建新的体检报告。
     * 
     * 业务流程：
     * 1. 接收前端传递的报告信息数据
     * 2. 进行数据验证和格式检查
     * 3. 验证报告编号的唯一性
     * 4. 验证关联的预约和检查项目
     * 5. 保存报告信息到数据库
     * 6. 记录操作日志
     * 7. 返回操作结果
     * 
     * 数据验证：
     * - 报告编号不能为空且必须唯一
     * - 关联的预约必须存在
     * - 检查项目必须有效
     * - 医生信息必须正确
     * 
     * 权限要求：
     * - 需要report:add权限
     * - 只有管理员和医生可以操作
     * 
     * @param dto 报告信息数据传输对象，包含报告的所有基本信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、数据库操作异常等
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加体检报告总")
    @PostMapping("/addReport")
    @Permission("report:add")
    public ApiResult addReport(@Valid @RequestBody ReportDto dto) {
        log.info("添加体检报告总：{}", dto);
        boolean flag = reportService.addReport(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改体检报告总
     * 
     * 功能说明：
     * 更新已存在的体检报告信息，支持修改报告的基本信息、检查结果、
     * 医生建议、状态等各项内容。
     * 
     * 业务流程：
     * 1. 接收前端传递的报告更新数据
     * 2. 验证报告ID的有效性
     * 3. 检查报告是否存在
     * 4. 进行数据格式验证
     * 5. 检查报告编号是否与其他报告重复
     * 6. 验证修改权限和业务规则
     * 7. 更新报告信息
     * 8. 记录操作日志
     * 9. 返回操作结果
     * 
     * 数据验证：
     * - 报告ID必须存在
     * - 修改后的报告编号不能与其他报告重复
     * - 检查项目变更需要验证合法性
     * - 医生信息变更需要验证权限
     * 
     * 特殊业务规则：
     * - 已发布的报告修改需要特殊权限
     * - 报告状态变更需要检查业务影响
     * - 某些关键信息可能需要上级审批
     * 
     * 权限要求：
     * - 需要report:update权限
     * - 只有管理员和负责医生可以操作
     * 
     * @param dto 报告信息数据传输对象，包含要更新的报告信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改体检报告总")
    @PostMapping("/updateReport")
    @Permission("report:update")
    public ApiResult updateReport(@Valid @RequestBody ReportDto dto) {
        log.info("修改体检报告总：{}", dto);
        boolean flag = reportService.updateReport(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除体检报告总
     * 
     * 功能说明：
     * 删除指定的体检报告信息。出于数据安全考虑，通常采用逻辑删除方式，
     * 即将报告状态标记为已删除，而不是物理删除数据记录。
     * 
     * 业务流程：
     * 1. 接收要删除的报告ID
     * 2. 验证报告ID的有效性
     * 3. 检查报告是否存在
     * 4. 验证是否可以删除（业务规则检查）
     * 5. 检查关联数据（预约、检查项目等）
     * 6. 执行删除操作（逻辑删除）
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * 数据验证：
     * - 报告ID必须存在
     * - 报告必须存在且未被删除
     * - 报告状态必须允许删除
     * 
     * 业务规则：
     * - 已发布的报告不能删除
     * - 有关联预约的报告需要特殊处理
     * - 删除操作需要高级权限
     * 
     * 权限要求：
     * - 需要report:delete权限
     * - 只有管理员可以操作
     * 
     * @param id 报告ID
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除体检报告总")
    @PostMapping("/deleteReport/{id}")
    @Permission("report:delete")
    public ApiResult deleteReport(@PathVariable Long id) {
        log.info("删除体检报告总：{}", id);
        boolean flag = reportService.deleteReport(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取体检报告总详情
     * 
     * 功能说明：
     * 根据报告ID查询并返回体检报告的详细信息，包括报告基本信息、
     * 检查项目、医生建议、状态等。
     * 
     * 业务流程：
     * 1. 接收报告ID参数
     * 2. 验证报告ID的有效性
     * 3. 检查报告是否存在
     * 4. 验证访问权限
     * 5. 查询报告详细信息
     * 6. 返回报告数据
     * 
     * 数据验证：
     * - 报告ID必须存在
     * - 报告必须存在且未被删除
     * 
     * 权限要求：
     * - 需要report:info权限
     * - 只有管理员和授权用户可以查看
     * 
     * @param id 报告ID
     * @return ApiResult<ReportVo> 返回报告详细信息
     * @throws Exception 可能抛出的异常包括数据验证异常、权限验证异常等
     */
    @Operation(summary = "获取体检报告总详情")
    @PostMapping("/getReport/{id}")
    @Permission("report:info")
    public ApiResult<ReportVo> getReport(@PathVariable Long id) {
        log.info("获取体检报告总详情：{}", id);
        ReportVo reportVo = reportService.getReportById(id);
        return ApiResult.success(reportVo);
    }

    /**
     * 获取体检报告总分页列表
     * 
     * 功能说明：
     * 支持多条件分页查询体检报告信息，提供灵活的数据检索功能。
     * 
     * 业务流程：
     * 1. 接收查询条件参数
     * 2. 验证查询条件的有效性
     * 3. 构建查询条件
     * 4. 执行分页查询
     * 5. 返回分页结果
     * 
     * 查询条件：
     * - 报告编号：支持模糊查询
     * - 用户信息：支持按用户筛选
     * - 医生信息：支持按医生筛选
     * - 报告状态：支持按状态筛选
     * - 时间范围：支持按时间筛选
     * 
     * 权限要求：
     * - 需要report:page权限
     * - 只有管理员和授权用户可以查询
     * 
     * @param query 查询条件对象，包含各种筛选条件
     * @return ApiResult<ReportVo> 返回分页报告列表
     * @throws Exception 可能抛出的异常包括数据验证异常、权限验证异常等
     */
    @Operation(summary = "获取体检报告总分页列表")
    @PostMapping("/getReportPage")
    @Permission("report:page")
    public ApiResult<ReportVo> getReportPage(@Valid @RequestBody ReportQuery query) {
        log.info("获取体检报告总分页列表：{}", query);
        Paging<ReportVo> paging = reportService.getReportPage(query);
        return ApiResult.success(paging);
    }

}
