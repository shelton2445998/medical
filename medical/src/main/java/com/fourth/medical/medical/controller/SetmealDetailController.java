package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.SetmealDetailDto;
import com.fourth.medical.medical.query.SetmealDetailQuery;
import com.fourth.medical.medical.service.SetmealDetailService;
import com.fourth.medical.medical.vo.SetmealDetailVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 体检套餐明细 控制器
 * 
 * 功能说明：
 * 本控制器是医疗体检系统中体检套餐明细管理的核心控制器，负责处理体检套餐明细的全生命周期管理，
 * 包括套餐明细的创建、修改、删除、查询等功能。体检套餐明细是体检套餐的组成部分，定义了套餐中
 * 包含的具体体检项目和相关信息。
 * 
 * 主要功能：
 * 1. 套餐明细基础管理
 *    - 新增体检套餐明细
 *    - 修改套餐明细信息
 *    - 删除套餐明细档案
 *    - 查询套餐明细详情
 *    - 套餐明细列表分页查询
 * 
 * 2. 套餐明细内容管理
 *    - 套餐明细项目配置
 *    - 套餐明细价格管理
 *    - 套餐明细状态控制
 *    - 套餐明细排序管理
 * 
 * 3. 套餐明细权限管理
 *    - 套餐明细创建权限控制
 *    - 套餐明细修改权限验证
 *    - 套餐明细删除权限检查
 *    - 套餐明细查看权限管理
 * 
 * 4. 套餐明细业务管理
 *    - 套餐明细关联管理
 *    - 套餐明细数据验证
 *    - 套餐明细业务规则
 *    - 套餐明细统计管理
 * 
 * 业务特点：
 * - 严格的权限控制，确保套餐明细信息的安全性
 * - 完整的操作日志记录，便于审计和问题追踪
 * - 数据完整性验证，确保套餐明细信息的准确性
 * - 支持复杂查询条件，提供灵活的数据检索功能
 * - 分页查询优化，提高大数据量处理性能
 * 
 * 权限体系：
 * - setmeal:detail:add - 新增套餐明细权限
 * - setmeal:detail:update - 修改套餐明细权限
 * - setmeal:detail:delete - 删除套餐明细权限
 * - setmeal:detail:info - 查看套餐明细信息权限
 * - setmeal:detail:page - 套餐明细列表查询权限
 * 
 * 数据安全：
 * - 套餐明细信息访问控制
 * - 敏感数据权限验证
 * - 操作权限严格检查
 * - 数据完整性校验
 * 
 * 关联模块：
 * - 体检套餐：套餐明细所属的套餐信息
 * - 体检项目：套餐明细包含的体检项目
 * - 价格管理：套餐明细的价格信息
 * - 订单管理：套餐明细的销售情况
 * 
 * @author fourth
 * @since 2025-07-09
 * @version 1.0
 */
@Slf4j
@RestController
@Tag(name = "体检套餐明细")
@RequestMapping("/admin/setmealDetail")
public class SetmealDetailController {

    @Autowired
    private SetmealDetailService setmealDetailService;

    /**
     * 添加体检套餐明细
     * 
     * 功能说明：
     * 新增体检套餐明细信息，包括套餐明细的基本信息、关联的体检项目、价格等。
     * 该接口用于医院管理人员为体检套餐添加具体的体检项目。
     * 
     * 业务流程：
     * 1. 接收前端传递的套餐明细信息数据
     * 2. 进行数据验证和格式检查
     * 3. 验证套餐ID的有效性
     * 4. 验证体检项目的有效性
     * 5. 检查套餐明细是否重复
     * 6. 保存套餐明细信息到数据库
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * 数据验证：
     * - 套餐ID必须存在且有效
     * - 体检项目ID必须存在且有效
     * - 套餐明细不能重复
     * - 价格信息必须合理
     * 
     * 权限要求：
     * - 需要setmeal:detail:add权限
     * - 只有管理员和套餐管理员可以操作
     * 
     * @param dto 套餐明细信息数据传输对象，包含套餐明细的所有基本信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、数据库操作异常等
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加体检套餐明细")
    @PostMapping("/addSetmealDetail")
    @Permission("setmeal:detail:add")
    public ApiResult addSetmealDetail(@Valid @RequestBody SetmealDetailDto dto) {
        log.info("添加体检套餐明细：{}", dto);
        boolean flag = setmealDetailService.addSetmealDetail(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改体检套餐明细
     * 
     * 功能说明：
     * 更新已存在的体检套餐明细信息，支持修改套餐明细的基本信息、关联的体检项目、
     * 价格、状态等各项内容。
     * 
     * 业务流程：
     * 1. 接收前端传递的套餐明细更新数据
     * 2. 验证套餐明细ID的有效性
     * 3. 检查套餐明细是否存在
     * 4. 进行数据格式验证
     * 5. 检查套餐明细是否与其他明细重复
     * 6. 验证修改权限和业务规则
     * 7. 更新套餐明细信息
     * 8. 记录操作日志
     * 9. 返回操作结果
     * 
     * 数据验证：
     * - 套餐明细ID必须存在
     * - 修改后的套餐明细不能重复
     * - 体检项目变更需要验证有效性
     * - 价格变更需要验证合理性
     * 
     * 特殊业务规则：
     * - 已销售的套餐明细修改需要特殊处理
     * - 套餐明细状态变更需要检查业务影响
     * - 某些关键信息可能需要上级审批
     * 
     * 权限要求：
     * - 需要setmeal:detail:update权限
     * - 只有管理员和套餐管理员可以操作
     * 
     * @param dto 套餐明细信息数据传输对象，包含要更新的套餐明细信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改体检套餐明细")
    @PostMapping("/updateSetmealDetail")
    @Permission("setmeal:detail:update")
    public ApiResult updateSetmealDetail(@Valid @RequestBody SetmealDetailDto dto) {
        log.info("修改体检套餐明细：{}", dto);
        boolean flag = setmealDetailService.updateSetmealDetail(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除体检套餐明细
     * 
     * 功能说明：
     * 删除指定的体检套餐明细信息。出于数据安全考虑，通常采用逻辑删除方式，
     * 即将套餐明细状态标记为已删除，而不是物理删除数据记录。
     * 
     * 业务流程：
     * 1. 接收要删除的套餐明细ID
     * 2. 验证套餐明细ID的有效性
     * 3. 检查套餐明细是否存在
     * 4. 验证是否可以删除（业务规则检查）
     * 5. 检查关联数据（订单等）
     * 6. 执行删除操作（逻辑删除）
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * 数据验证：
     * - 套餐明细ID必须存在
     * - 套餐明细必须存在且未被删除
     * - 套餐明细状态必须允许删除
     * 
     * 业务规则：
     * - 已销售的套餐明细不能删除
     * - 有关联订单的套餐明细需要特殊处理
     * - 删除操作需要高级权限
     * 
     * 权限要求：
     * - 需要setmeal:detail:delete权限
     * - 只有管理员可以操作
     * 
     * @param id 套餐明细ID
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除体检套餐明细")
    @PostMapping("/deleteSetmealDetail/{id}")
    @Permission("setmeal:detail:delete")
    public ApiResult deleteSetmealDetail(@PathVariable Long id) {
        log.info("删除体检套餐明细：{}", id);
        boolean flag = setmealDetailService.deleteSetmealDetail(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取体检套餐明细详情
     * 
     * 功能说明：
     * 根据套餐明细ID查询并返回体检套餐明细的详细信息，包括套餐明细基本信息、
     * 关联的体检项目、价格、状态等。
     * 
     * 业务流程：
     * 1. 接收套餐明细ID参数
     * 2. 验证套餐明细ID的有效性
     * 3. 检查套餐明细是否存在
     * 4. 验证访问权限
     * 5. 查询套餐明细详细信息
     * 6. 返回套餐明细数据
     * 
     * 数据验证：
     * - 套餐明细ID必须存在
     * - 套餐明细必须存在且未被删除
     * 
     * 权限要求：
     * - 需要setmeal:detail:info权限
     * - 只有管理员和授权用户可以查看
     * 
     * @param id 套餐明细ID
     * @return ApiResult<SetmealDetailVo> 返回套餐明细详细信息
     * @throws Exception 可能抛出的异常包括数据验证异常、权限验证异常等
     */
    @Operation(summary = "获取体检套餐明细详情")
    @PostMapping("/getSetmealDetail/{id}")
    @Permission("setmeal:detail:info")
    public ApiResult<SetmealDetailVo> getSetmealDetail(@PathVariable Long id) {
        log.info("获取体检套餐明细详情：{}", id);
        SetmealDetailVo setmealDetailVo = setmealDetailService.getSetmealDetailById(id);
        return ApiResult.success(setmealDetailVo);
    }

    /**
     * 获取体检套餐明细分页列表
     * 
     * 功能说明：
     * 支持多条件分页查询体检套餐明细信息，提供灵活的数据检索功能。
     * 
     * 业务流程：
     * 1. 接收查询条件参数
     * 2. 验证查询条件的有效性
     * 3. 构建查询条件
     * 4. 执行分页查询
     * 5. 返回分页结果
     * 
     * 查询条件：
     * - 套餐ID：支持按套餐筛选
     * - 体检项目ID：支持按体检项目筛选
     * - 套餐明细状态：支持按状态筛选
     * - 价格范围：支持按价格范围筛选
     * - 创建时间：支持按时间筛选
     * 
     * 权限要求：
     * - 需要setmeal:detail:page权限
     * - 只有管理员和授权用户可以查询
     * 
     * @param query 查询条件对象，包含各种筛选条件
     * @return ApiResult<SetmealDetailVo> 返回分页套餐明细列表
     * @throws Exception 可能抛出的异常包括数据验证异常、权限验证异常等
     */
    @Operation(summary = "获取体检套餐明细分页列表")
    @PostMapping("/getSetmealDetailPage")
    @Permission("setmeal:detail:page")
    public ApiResult<SetmealDetailVo> getSetmealDetailPage(@Valid @RequestBody SetmealDetailQuery query) {
        log.info("获取体检套餐明细分页列表：{}", query);
        Paging<SetmealDetailVo> paging = setmealDetailService.getSetmealDetailPage(query);
        return ApiResult.success(paging);
    }

}
