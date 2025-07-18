package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.OrdersDto;
import com.fourth.medical.medical.query.OrdersQuery;
import com.fourth.medical.medical.service.OrdersService;
import com.fourth.medical.medical.vo.OrdersVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 体检预约订单 控制器
 * 
 * 功能说明：
 * 本控制器是医疗体检系统中医生端预约订单管理的核心控制器，负责处理医生对体检预约订单的
 * 各种操作，包括创建、修改、删除、查询预约订单等功能。医生可以通过此控制器管理自己负责的
 * 体检预约，确保预约流程的顺利进行。
 * 
 * 主要功能：
 * 1. 预约订单基础管理
 *    - 新增体检预约订单
 *    - 修改预约订单信息
 *    - 删除预约订单档案
 *    - 查询预约订单详情
 *    - 预约订单列表分页查询
 * 
 * 2. 预约状态管理
 *    - 预约状态更新控制
 *    - 预约状态流转管理
 *    - 预约状态监控
 *    - 预约异常状态处理
 * 
 * 3. 预约权限管理
 *    - 预约创建权限控制
 *    - 预约修改权限验证
 *    - 预约删除权限检查
 *    - 预约查看权限管理
 * 
 * 4. 预约业务管理
 *    - 预约时间安排
 *    - 预约容量管理
 *    - 预约冲突检测
 *    - 预约通知管理
 * 
 * 业务特点：
 * - 严格的权限控制，确保预约信息的安全性
 * - 完整的操作日志记录，便于审计和问题追踪
 * - 数据完整性验证，确保预约信息的准确性
 * - 支持复杂查询条件，提供灵活的数据检索功能
 * - 分页查询优化，提高大数据量处理性能
 * 
 * 权限体系：
 * - orders:add - 新增预约权限
 * - orders:update - 修改预约权限
 * - orders:delete - 删除预约权限
 * - orders:info - 查看预约信息权限
 * - orders:page - 预约列表查询权限
 * 
 * 数据安全：
 * - 预约信息访问控制
 * - 敏感数据权限验证
 * - 操作权限严格检查
 * - 数据完整性校验
 * 
 * 关联模块：
 * - 用户管理：预约关联的用户信息
 * - 体检套餐：预约选择的体检套餐
 * - 医生排班：预约关联的医生排班
 * - 医院管理：预约所属的医院信息
 * 
 * @author fourth
 * @since 2025-07-09
 * @version 1.0
 */
@Slf4j
@RestController
@Tag(name = "体检预约订单")
@RequestMapping("/doctor/appointment")
public class DoctorOrders {

    @Autowired
    private OrdersService ordersService;

    /**
     * 添加体检预约订单
     * 
     * 功能说明：
     * 新增体检预约订单信息，包括预约的基本信息、用户信息、体检套餐、预约时间等。
     * 该接口用于医生为用户创建新的体检预约。
     * 
     * 业务流程：
     * 1. 接收前端传递的预约信息数据
     * 2. 进行数据验证和格式检查
     * 3. 验证用户信息的有效性
     * 4. 检查预约时间的可用性
     * 5. 验证体检套餐的有效性
     * 6. 保存预约信息到数据库
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * 数据验证：
     * - 用户信息必须完整且有效
     * - 预约时间不能冲突
     * - 体检套餐必须存在且有效
     * - 预约容量不能超限
     * 
     * 权限要求：
     * - 需要orders:add权限
     * - 只有医生和管理员可以操作
     * 
     * @param dto 预约信息数据传输对象，包含预约的所有基本信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、数据库操作异常等
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加体检预约订单")
    @PostMapping("/add")
    @Permission("orders:add")
    public ApiResult addOrders(@Valid @RequestBody OrdersDto dto) {
        log.info("添加体检预约订单：{}", dto);
        boolean flag = ordersService.addOrders(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改体检预约订单
     * 
     * 功能说明：
     * 更新已存在的体检预约订单信息，支持修改预约的基本信息、预约时间、
     * 体检套餐、状态等各项内容。
     * 
     * 业务流程：
     * 1. 接收前端传递的预约更新数据
     * 2. 验证预约ID的有效性
     * 3. 检查预约是否存在
     * 4. 进行数据格式验证
     * 5. 检查预约时间是否冲突
     * 6. 验证修改权限和业务规则
     * 7. 更新预约信息
     * 8. 记录操作日志
     * 9. 返回操作结果
     * 
     * 数据验证：
     * - 预约ID必须存在
     * - 修改后的时间不能冲突
     * - 体检套餐变更需要验证有效性
     * - 用户信息变更需要验证权限
     * 
     * 特殊业务规则：
     * - 已确认的预约修改需要特殊处理
     * - 预约状态变更需要检查业务影响
     * - 某些关键信息可能需要上级审批
     * 
     * 权限要求：
     * - 需要orders:update权限
     * - 只有医生和管理员可以操作
     * 
     * @param dto 预约信息数据传输对象，包含要更新的预约信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改体检预约订单")
    @PutMapping("/update")
    @Permission("orders:update")
    public ApiResult updateOrders(@Valid @RequestBody OrdersDto dto) {
        log.info("修改体检预约订单：{}", dto);
        boolean flag = ordersService.updateOrders(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除体检预约订单
     * 
     * 功能说明：
     * 删除指定的体检预约订单信息。出于数据安全考虑，通常采用逻辑删除方式，
     * 即将预约状态标记为已删除，而不是物理删除数据记录。
     * 
     * 业务流程：
     * 1. 接收要删除的预约ID
     * 2. 验证预约ID的有效性
     * 3. 检查预约是否存在
     * 4. 验证是否可以删除（业务规则检查）
     * 5. 检查关联数据（体检报告等）
     * 6. 执行删除操作（逻辑删除）
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * 数据验证：
     * - 预约ID必须存在
     * - 预约必须存在且未被删除
     * - 预约状态必须允许删除
     * 
     * 业务规则：
     * - 已确认的预约不能删除
     * - 有关联体检报告的预约需要特殊处理
     * - 删除操作需要高级权限
     * 
     * 权限要求：
     * - 需要orders:delete权限
     * - 只有医生和管理员可以操作
     * 
     * @param id 预约ID
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除体检预约订单")
    @DeleteMapping("/delete/{id}")
    @Permission("orders:delete")
    public ApiResult deleteOrders(@PathVariable Long id) {
        log.info("删除体检预约订单：{}", id);
        boolean flag = ordersService.deleteOrders(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取体检预约订单详情
     * 
     * 功能说明：
     * 根据预约ID查询并返回体检预约订单的详细信息，包括预约基本信息、
     * 用户信息、体检套餐、预约时间等。
     * 
     * 业务流程：
     * 1. 接收预约ID参数
     * 2. 验证预约ID的有效性
     * 3. 检查预约是否存在
     * 4. 验证访问权限
     * 5. 查询预约详细信息
     * 6. 返回预约数据
     * 
     * 数据验证：
     * - 预约ID必须存在
     * - 预约必须存在且未被删除
     * 
     * 权限要求：
     * - 需要orders:info权限
     * - 只有医生和授权用户可以查看
     * 
     * @param id 预约ID
     * @return ApiResult<OrdersVo> 返回预约详细信息
     * @throws Exception 可能抛出的异常包括数据验证异常、权限验证异常等
     */
    @Operation(summary = "获取体检预约订单详情")
    @GetMapping("/detail/{appointmentId}")
    @Permission("orders:info")
    public ApiResult<OrdersVo> getOrders(@PathVariable("appointmentId") Long id) {
        log.info("获取体检预约订单详情：{}", id);
        OrdersVo ordersVo = ordersService.getOrdersById(id);
        return ApiResult.success(ordersVo);
    }

    /**
     * 获取体检预约订单列表
     * 
     * 功能说明：
     * 支持多条件分页查询体检预约订单信息，提供灵活的数据检索功能。
     * 
     * 业务流程：
     * 1. 接收查询条件参数
     * 2. 验证查询条件的有效性
     * 3. 构建查询条件
     * 4. 执行分页查询
     * 5. 返回分页结果
     * 
     * 查询条件：
     * - 用户信息：支持按用户筛选
     * - 体检套餐：支持按套餐筛选
     * - 预约状态：支持按状态筛选
     * - 预约时间：支持按时间筛选
     * - 医生信息：支持按医生筛选
     * 
     * 权限要求：
     * - 需要orders:page权限
     * - 只有医生和授权用户可以查询
     * 
     * @param query 查询条件对象，包含各种筛选条件
     * @return ApiResult<OrdersVo> 返回分页预约列表
     * @throws Exception 可能抛出的异常包括数据验证异常、权限验证异常等
     */
    @Operation(summary = "获取体检预约订单列表")
    @GetMapping("/list")
    @Permission("orders:page")
    public ApiResult<OrdersVo> getOrdersList(@Valid OrdersQuery query) {
        log.info("获取体检预约订单列表：{}", query);
        Paging<OrdersVo> paging = ordersService.getOrdersPage(query);
        return ApiResult.success(paging);
    }

    /**
     * 更新预约状态
     * 
     * 功能说明：
     * 更新指定预约订单的状态，支持预约状态的状态流转管理。
     * 
     * 业务流程：
     * 1. 接收预约ID和状态参数
     * 2. 验证预约ID的有效性
     * 3. 检查预约是否存在
     * 4. 验证状态变更的合法性
     * 5. 更新预约状态
     * 6. 记录操作日志
     * 7. 返回操作结果
     * 
     * 数据验证：
     * - 预约ID必须存在
     * - 预约必须存在且未被删除
     * - 状态值必须在有效范围内
     * - 状态变更必须符合业务规则
     * 
     * 权限要求：
     * - 需要orders:update权限
     * - 只有医生和管理员可以操作
     * 
     * @param id 预约ID
     * @param status 新的预约状态
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "更新预约状态")
    @PutMapping("/status")
    @Permission("orders:update")
    public ApiResult updateOrdersStatus(@RequestParam Long id, @RequestParam Integer status) {
        log.info("更新预约状态：id={}, status={}", id, status);
        OrdersDto dto = new OrdersDto();
        dto.setId(id);
        dto.setStatus(status);
        boolean flag = ordersService.updateOrders(dto);
        return ApiResult.result(flag);
    }

}
