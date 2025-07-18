package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.OverallResultDto;
import com.fourth.medical.medical.query.OverallResultQuery;
import com.fourth.medical.medical.service.OverallResultService;
import com.fourth.medical.medical.vo.OverallResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 总检结论 控制器
 * 
 * 功能说明：
 * 本控制器是医疗体检系统中总检结论管理的核心控制器，负责处理体检总检结论的全生命周期管理，
 * 包括总检结论的创建、修改、删除、查询等功能。总检结论是体检报告的最终总结，由总检医生
 * 根据各项检查结果综合分析后给出，是体检服务的重要成果。
 * 
 * 主要功能：
 * 1. 总检结论基础管理
 *    - 新增总检结论
 *    - 修改总检结论信息
 *    - 删除总检结论档案
 *    - 查询总检结论详情
 *    - 总检结论列表分页查询
 * 
 * 2. 总检结论内容管理
 *    - 总检结论内容编辑
 *    - 总检结论状态管理
 *    - 总检结论审核流程
 *    - 总检结论发布管理
 * 
 * 3. 总检结论权限管理
 *    - 总检结论创建权限控制
 *    - 总检结论修改权限验证
 *    - 总检结论删除权限检查
 *    - 总检结论查看权限管理
 * 
 * 4. 总检结论业务管理
 *    - 总检结论关联管理
 *    - 总检结论数据验证
 *    - 总检结论业务规则
 *    - 总检结论统计分析
 * 
 * 业务特点：
 * - 严格的权限控制，确保总检结论信息的安全性
 * - 完整的操作日志记录，便于审计和问题追踪
 * - 数据完整性验证，确保总检结论信息的准确性
 * - 支持复杂查询条件，提供灵活的数据检索功能
 * - 分页查询优化，提高大数据量处理性能
 * 
 * 权限体系：
 * - overall:result:add - 新增总检结论权限
 * - overall:result:update - 修改总检结论权限
 * - overall:result:delete - 删除总检结论权限
 * - overall:result:info - 查看总检结论信息权限
 * - overall:result:page - 总检结论列表查询权限
 * 
 * 数据安全：
 * - 总检结论信息访问控制
 * - 敏感数据权限验证
 * - 操作权限严格检查
 * - 数据完整性校验
 * 
 * 关联模块：
 * - 体检报告：总检结论所属的体检报告
 * - 医生管理：总检结论负责的医生信息
 * - 用户管理：总检结论所属的用户信息
 * - 检查项目：总检结论基于的检查结果
 * 
 * @author fourth
 * @since 2025-07-09
 * @version 1.0
 */
@Slf4j
@RestController
@Tag(name = "总检结论")
@RequestMapping("/admin/overallResult")
public class OverallResultController {

    @Autowired
    private OverallResultService overallResultService;

    /**
     * 添加总检结论
     * 
     * 功能说明：
     * 新增总检结论信息，包括总检结论的基本信息、结论内容、医生建议等。
     * 该接口用于总检医生为体检报告创建总检结论。
     * 
     * 业务流程：
     * 1. 接收前端传递的总检结论信息数据
     * 2. 进行数据验证和格式检查
     * 3. 验证体检报告ID的有效性
     * 4. 验证总检医生信息的有效性
     * 5. 检查总检结论是否已存在
     * 6. 保存总检结论信息到数据库
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * 数据验证：
     * - 体检报告ID必须存在且有效
     * - 总检医生ID必须存在且有效
     * - 总检结论内容不能为空
     * - 总检结论不能重复
     * 
     * 权限要求：
     * - 需要overall:result:add权限
     * - 只有总检医生和管理员可以操作
     * 
     * @param dto 总检结论信息数据传输对象，包含总检结论的所有基本信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、数据库操作异常等
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加总检结论")
    @PostMapping("/addOverallResult")
    @Permission("overall:result:add")
    public ApiResult addOverallResult(@Valid @RequestBody OverallResultDto dto) {
        log.info("添加总检结论：{}", dto);
        boolean flag = overallResultService.addOverallResult(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改总检结论
     * 
     * 功能说明：
     * 更新已存在的总检结论信息，支持修改总检结论的基本信息、结论内容、
     * 医生建议、状态等各项内容。
     * 
     * 业务流程：
     * 1. 接收前端传递的总检结论更新数据
     * 2. 验证总检结论ID的有效性
     * 3. 检查总检结论是否存在
     * 4. 进行数据格式验证
     * 5. 检查修改权限和业务规则
     * 6. 更新总检结论信息
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * 数据验证：
     * - 总检结论ID必须存在
     * - 总检结论内容不能为空
     * - 医生信息变更需要验证权限
     * - 状态变更需要验证业务规则
     * 
     * 特殊业务规则：
     * - 已发布的总检结论修改需要特殊权限
     * - 总检结论状态变更需要检查业务影响
     * - 某些关键信息可能需要上级审批
     * 
     * 权限要求：
     * - 需要overall:result:update权限
     * - 只有总检医生和管理员可以操作
     * 
     * @param dto 总检结论信息数据传输对象，包含要更新的总检结论信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改总检结论")
    @PostMapping("/updateOverallResult")
    @Permission("overall:result:update")
    public ApiResult updateOverallResult(@Valid @RequestBody OverallResultDto dto) {
        log.info("修改总检结论：{}", dto);
        boolean flag = overallResultService.updateOverallResult(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除总检结论
     * 
     * 功能说明：
     * 删除指定的总检结论信息。出于数据安全考虑，通常采用逻辑删除方式，
     * 即将总检结论状态标记为已删除，而不是物理删除数据记录。
     * 
     * 业务流程：
     * 1. 接收要删除的总检结论ID
     * 2. 验证总检结论ID的有效性
     * 3. 检查总检结论是否存在
     * 4. 验证是否可以删除（业务规则检查）
     * 5. 检查关联数据（体检报告等）
     * 6. 执行删除操作（逻辑删除）
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * 数据验证：
     * - 总检结论ID必须存在
     * - 总检结论必须存在且未被删除
     * - 总检结论状态必须允许删除
     * 
     * 业务规则：
     * - 已发布的总检结论不能删除
     * - 有关联体检报告的总检结论需要特殊处理
     * - 删除操作需要高级权限
     * 
     * 权限要求：
     * - 需要overall:result:delete权限
     * - 只有管理员可以操作
     * 
     * @param id 总检结论ID
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除总检结论")
    @PostMapping("/deleteOverallResult/{id}")
    @Permission("overall:result:delete")
    public ApiResult deleteOverallResult(@PathVariable Long id) {
        log.info("删除总检结论：{}", id);
        boolean flag = overallResultService.deleteOverallResult(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取总检结论详情
     * 
     * 功能说明：
     * 根据总检结论ID查询并返回总检结论的详细信息，包括总检结论基本信息、
     * 结论内容、医生建议、状态等。
     * 
     * 业务流程：
     * 1. 接收总检结论ID参数
     * 2. 验证总检结论ID的有效性
     * 3. 检查总检结论是否存在
     * 4. 验证访问权限
     * 5. 查询总检结论详细信息
     * 6. 返回总检结论数据
     * 
     * 数据验证：
     * - 总检结论ID必须存在
     * - 总检结论必须存在且未被删除
     * 
     * 权限要求：
     * - 需要overall:result:info权限
     * - 只有管理员和授权用户可以查看
     * 
     * @param id 总检结论ID
     * @return ApiResult<OverallResultVo> 返回总检结论详细信息
     * @throws Exception 可能抛出的异常包括数据验证异常、权限验证异常等
     */
    @Operation(summary = "获取总检结论详情")
    @PostMapping("/getOverallResult/{id}")
    @Permission("overall:result:info")
    public ApiResult<OverallResultVo> getOverallResult(@PathVariable Long id) {
        log.info("获取总检结论详情：{}", id);
        OverallResultVo overallResultVo = overallResultService.getOverallResultById(id);
        return ApiResult.success(overallResultVo);
    }

    /**
     * 获取总检结论分页列表
     * 
     * 功能说明：
     * 支持多条件分页查询总检结论信息，提供灵活的数据检索功能。
     * 
     * 业务流程：
     * 1. 接收查询条件参数
     * 2. 验证查询条件的有效性
     * 3. 构建查询条件
     * 4. 执行分页查询
     * 5. 返回分页结果
     * 
     * 查询条件：
     * - 体检报告ID：支持按体检报告筛选
     * - 总检医生ID：支持按医生筛选
     * - 总检结论状态：支持按状态筛选
     * - 创建时间：支持按时间筛选
     * - 用户信息：支持按用户筛选
     * 
     * 权限要求：
     * - 需要overall:result:page权限
     * - 只有管理员和授权用户可以查询
     * 
     * @param query 查询条件对象，包含各种筛选条件
     * @return ApiResult<OverallResultVo> 返回分页总检结论列表
     * @throws Exception 可能抛出的异常包括数据验证异常、权限验证异常等
     */
    @Operation(summary = "获取总检结论分页列表")
    @PostMapping("/getOverallResultPage")
    @Permission("overall:result:page")
    public ApiResult<OverallResultVo> getOverallResultPage(@Valid @RequestBody OverallResultQuery query) {
        log.info("获取总检结论分页列表：{}", query);
        Paging<OverallResultVo> paging = overallResultService.getOverallResultPage(query);
        return ApiResult.success(paging);
    }

}
