package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.SetmealDto;
import com.fourth.medical.medical.query.SetmealQuery;
import com.fourth.medical.medical.service.SetmealService;
import com.fourth.medical.medical.vo.SetmealVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 体检套餐管理控制器
 * 
 * 功能说明：
 * 本控制器是医疗体检系统中体检套餐管理的核心控制器，负责处理体检套餐的全生命周期管理。
 * 体检套餐是体检服务的核心产品，包含多个体检项目的组合，为用户提供完整的体检服务方案。
 * 该控制器提供了套餐的创建、维护、定价、状态管理等完整功能。
 * 
 * 主要功能：
 * 1. 套餐基础信息管理
 *    - 新增体检套餐
 *    - 修改套餐信息
 *    - 删除套餐档案
 *    - 查询套餐详情
 *    - 套餐列表分页查询
 * 
 * 2. 套餐内容管理
 *    - 套餐体检项目配置
 *    - 套餐项目组合管理
 *    - 套餐服务范围设置
 *    - 套餐适用人群定义
 * 
 * 3. 套餐定价管理
 *    - 套餐价格设置
 *    - 套餐优惠策略
 *    - 套餐成本控制
 *    - 套餐盈利分析
 * 
 * 4. 套餐状态管理
 *    - 套餐上下架控制
 *    - 套餐销售状态管理
 *    - 套餐推荐状态设置
 *    - 套餐库存管理
 * 
 * 业务特点：
 * - 灵活的套餐配置，支持多种体检项目组合
 * - 完整的定价体系，支持多种定价策略
 * - 精细的权限控制，确保套餐管理的安全性
 * - 完整的操作日志记录，便于审计和分析
 * - 支持套餐个性化定制和推荐
 * 
 * 权限体系：
 * - setmeal:add - 新增套餐权限
 * - setmeal:update - 修改套餐权限
 * - setmeal:delete - 删除套餐权限
 * - setmeal:info - 查看套餐信息权限
 * - setmeal:page - 套餐列表查询权限
 * 
 * 数据安全：
 * - 套餐定价信息保护
 * - 套餐成本数据访问控制
 * - 套餐销售数据隐私保护
 * - 套餐配置变更记录
 * 
 * 关联模块：
 * - 体检项目：套餐包含的具体检查项目
 * - 预约系统：套餐的预约和购买
 * - 订单系统：套餐的销售和订单管理
 * - 支付系统：套餐的支付和结算
 * 
 * 业务规则：
 * - 套餐必须包含至少一个体检项目
 * - 套餐价格不能低于项目成本
 * - 有订单的套餐不能直接删除
 * - 套餐状态变更需要验证业务影响
 * 
 * @author fourth
 * @since 2025-07-09
 * @version 1.0
 */
@Slf4j  // 自动生成日志对象log
@RestController  // 标识这是一个REST控制器，返回JSON格式数据
@Tag(name = "体检套餐")  // Swagger文档标签，用于API分组
@RequestMapping("/admin/package")  // 设置控制器的基础请求路径
public class SetmealController {

    /**
     * 体检套餐服务接口
     * 提供套餐信息的业务逻辑处理，包括套餐配置、定价、状态管理等
     */
    @Autowired
    private SetmealService setmealService;

    /**
     * 添加体检套餐
     * 
     * 功能说明：
     * 新增体检套餐信息，包括套餐的基本信息、包含的体检项目、定价策略等。
     * 该接口用于医院管理人员创建新的体检套餐产品。
     * 
     * 业务流程：
     * 1. 接收套餐基础信息
     * 2. 验证套餐名称和编码唯一性
     * 3. 验证套餐包含的体检项目
     * 4. 计算套餐价格和成本
     * 5. 验证套餐业务规则
     * 6. 保存套餐信息
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * @param dto 套餐信息数据传输对象
     * @return ApiResult 操作结果
     * @throws Exception 可能抛出的异常
     */
    @Log(type = SysLogType.ADD)  // 记录操作日志
    @Operation(summary = "添加体检套餐")  // Swagger接口描述
    @PostMapping("/add")  // HTTP POST请求映射
    @Permission("setmeal:add")  // 权限验证
    public ApiResult addSetmeal(@Valid @RequestBody SetmealDto dto) {
        log.info("添加体检套餐：{}", dto);
        boolean flag = setmealService.addSetmeal(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改体检套餐
     * 
     * 功能说明：
     * 更新已存在的体检套餐信息，支持修改套餐的基本信息、体检项目、
     * 定价策略、状态等各项内容。
     * 
     * 业务流程：
     * 1. 验证套餐ID的有效性
     * 2. 检查套餐是否存在
     * 3. 验证修改权限
     * 4. 检查套餐是否有关联订单
     * 5. 验证修改内容的合法性
     * 6. 更新套餐信息
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * @param dto 套餐信息数据传输对象
     * @return ApiResult 操作结果
     * @throws Exception 可能抛出的异常
     */
    @Log(type = SysLogType.UPDATE)  // 记录操作日志
    @Operation(summary = "修改体检套餐")  // Swagger接口描述
    @PutMapping("/update")  // HTTP PUT请求映射
    @Permission("setmeal:update")  // 权限验证
    public ApiResult updateSetmeal(@Valid @RequestBody SetmealDto dto) {
        log.info("修改体检套餐：{}", dto);
        boolean flag = setmealService.updateSetmeal(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除体检套餐
     * 
     * 功能说明：
     * 删除指定的体检套餐信息。出于业务连续性考虑，
     * 通常采用逻辑删除方式，并需要进行业务规则检查。
     * 
     * 业务流程：
     * 1. 验证套餐ID的有效性
     * 2. 检查套餐是否存在
     * 3. 检查套餐是否有关联订单
     * 4. 检查套餐是否有预约记录
     * 5. 执行删除操作
     * 6. 记录操作日志
     * 7. 返回操作结果
     * 
     * @param id 套餐ID
     * @return ApiResult 操作结果
     * @throws Exception 可能抛出的异常
     */
    @Log(type = SysLogType.DELETE)  // 记录操作日志
    @Operation(summary = "删除体检套餐")  // Swagger接口描述
    @DeleteMapping("/delete/{packageId}")  // HTTP DELETE请求映射
    @Permission("setmeal:delete")  // 权限验证
    public ApiResult deleteSetmeal(@PathVariable("packageId") Long id) {
        log.info("删除体检套餐：{}", id);
        boolean flag = setmealService.deleteSetmeal(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取体检套餐详情
     * 
     * 功能说明：
     * 根据套餐ID查询并返回体检套餐的详细信息，包括套餐基本信息、
     * 包含的体检项目、定价信息、销售统计等。
     * 
     * 业务流程：
     * 1. 验证套餐ID的有效性
     * 2. 查询套餐基本信息
     * 3. 查询套餐项目详情
     * 4. 查询套餐价格信息
     * 5. 查询套餐销售统计
     * 6. 组装完整信息
     * 7. 返回查询结果
     * 
     * @param id 套餐ID
     * @return ApiResult<SetmealVo> 套餐详细信息
     * @throws Exception 可能抛出的异常
     */
    @Operation(summary = "获取体检套餐详情")  // Swagger接口描述
    @GetMapping("/detail/{packageId}")  // HTTP GET请求映射
    @Permission("setmeal:info")  // 权限验证
    public ApiResult<SetmealVo> getSetmeal(@PathVariable("packageId") Long id) {
        log.info("获取体检套餐详情：{}", id);
        SetmealVo setmealVo = setmealService.getSetmealById(id);
        return ApiResult.success(setmealVo);
    }

    /**
     * 获取体检套餐列表
     * 
     * 功能说明：
     * 根据查询条件分页查询体检套餐列表，支持多种查询条件组合，
     * 为管理界面提供灵活的套餐信息检索功能。
     * 
     * 业务流程：
     * 1. 接收查询条件和分页参数
     * 2. 验证查询参数
     * 3. 构建查询条件
     * 4. 执行分页查询
     * 5. 处理查询结果
     * 6. 计算分页信息
     * 7. 返回分页结果
     * 
     * @param query 查询条件对象
     * @return ApiResult<SetmealVo> 套餐分页数据
     * @throws Exception 可能抛出的异常
     */
    @Operation(summary = "获取体检套餐列表")  // Swagger接口描述
    @GetMapping("/list")  // HTTP GET请求映射
    @Permission("setmeal:page")  // 权限验证
    public ApiResult<SetmealVo> getSetmealList(@Valid SetmealQuery query) {
        log.info("获取体检套餐列表：{}", query);
        Paging<SetmealVo> paging = setmealService.getSetmealPage(query);
        return ApiResult.success(paging);
    }

    /**
     * 更新体检套餐状态
     * 
     * 功能说明：
     * 快速更新体检套餐的销售状态，支持上架/下架套餐。
     * 主要用于套餐销售状态的动态管理。
     * 
     * 业务流程：
     * 1. 验证套餐ID和状态参数
     * 2. 检查套餐是否存在
     * 3. 验证状态变更权限
     * 4. 检查状态变更的业务影响
     * 5. 更新套餐状态
     * 6. 记录操作日志
     * 7. 返回操作结果
     * 
     * @param id 套餐ID
     * @param status 套餐状态
     * @return ApiResult 操作结果
     * @throws Exception 可能抛出的异常
     */
    @Log(type = SysLogType.UPDATE)  // 记录操作日志
    @Operation(summary = "更新体检套餐状态")  // Swagger接口描述
    @PutMapping("/status")  // HTTP PUT请求映射
    @Permission("setmeal:update")  // 权限验证
    public ApiResult updateSetmealStatus(@RequestParam Long id, @RequestParam Boolean status) {
        log.info("更新体检套餐状态：id={}, status={}", id, status);
        SetmealDto dto = new SetmealDto();
        dto.setId(id);
        dto.setStatus(status);
        boolean flag = setmealService.updateSetmeal(dto);
        return ApiResult.result(flag);
    }

}
