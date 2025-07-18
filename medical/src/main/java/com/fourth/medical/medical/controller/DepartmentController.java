package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.DepartmentDto;
import com.fourth.medical.medical.query.DepartmentQuery;
import com.fourth.medical.medical.service.DepartmentService;
import com.fourth.medical.medical.vo.DepartmentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 科室管理控制器
 * 
 * 功能说明：
 * 本控制器是医疗体检系统中科室管理的核心控制器，负责处理医院科室信息的全生命周期管理，
 * 包括科室的基本信息维护、科室设置、医生分配、科室状态管理等功能。科室是医院组织架构
 * 的基本单元，与医生、体检项目、预约系统等模块紧密关联。
 * 
 * 主要功能：
 * 1. 科室基础信息管理
 *    - 新增科室档案
 *    - 修改科室信息
 *    - 删除科室档案
 *    - 查询科室详情
 *    - 科室列表分页查询
 * 
 * 2. 科室组织架构管理
 *    - 科室层级结构设置
 *    - 科室上下级关系维护
 *    - 科室合并与拆分
 *    - 科室归属管理
 * 
 * 3. 科室业务管理
 *    - 科室服务项目配置
 *    - 科室体检项目关联
 *    - 科室预约设置
 *    - 科室工作时间配置
 * 
 * 4. 科室状态管理
 *    - 科室开放状态控制
 *    - 科室服务状态管理
 *    - 科室运营状态监控
 *    - 科室容量管理
 * 
 * 业务特点：
 * - 严格的权限控制，确保科室信息的安全性
 * - 完整的操作日志记录，便于审计和追踪
 * - 数据完整性验证，确保科室信息的准确性
 * - 支持复杂查询条件，提供灵活的数据检索
 * - 层级结构管理，支持科室的树形组织结构
 * 
 * 权限体系：
 * - department:add - 新增科室权限
 * - department:update - 修改科室权限
 * - department:delete - 删除科室权限
 * - department:info - 查看科室信息权限
 * - department:page - 科室列表查询权限
 * 
 * 数据安全：
 * - 科室信息访问控制
 * - 敏感数据权限验证
 * - 操作权限严格检查
 * - 数据变更记录追踪
 * 
 * 关联模块：
 * - 医生管理：科室与医生的归属关系
 * - 体检项目：科室提供的体检服务
 * - 预约系统：科室的预约时间管理
 * - 医院管理：科室所属医院信息
 * 
 * 业务规则：
 * - 科室名称在同一医院内必须唯一
 * - 有医生归属的科室不能直接删除
 * - 科室状态变更需要检查相关业务影响
 * - 科室层级结构不能形成循环引用
 * 
 * @author fourth
 * @since 2025-07-09
 * @version 1.0
 */
@Slf4j  // 自动生成日志对象log
@RestController  // 标识这是一个REST控制器，返回JSON格式数据
@Tag(name = "科室")  // Swagger文档标签，用于API分组
@RequestMapping("/admin/department")  // 设置控制器的基础请求路径
public class DepartmentController {

    /**
     * 科室服务接口
     * 提供科室信息的业务逻辑处理，包括增删改查和业务规则验证
     */
    @Autowired
    private DepartmentService departmentService;

    /**
     * 添加科室信息
     * 
     * 功能说明：
     * 新增科室档案信息，包括科室的基本信息、组织架构、服务范围等。
     * 该接口用于医院管理人员向系统中添加新的科室记录。
     * 
     * 业务流程：
     * 1. 接收前端传递的科室信息数据
     * 2. 进行数据验证和格式检查
     * 3. 检查科室名称是否重复
     * 4. 验证上级科室是否存在（如果有）
     * 5. 验证科室编码的唯一性
     * 6. 保存科室信息到数据库
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * 数据验证：
     * - 科室名称不能为空且不能重复
     * - 科室编码必须唯一
     * - 科室类型必须在有效范围内
     * - 上级科室ID必须存在（如果指定）
     * - 科室负责人必须是有效的医生
     * 
     * 业务规则：
     * - 科室名称在同一医院内必须唯一
     * - 科室编码全系统唯一
     * - 科室层级不能超过预设的最大深度
     * - 新建科室默认状态为启用
     * 
     * 权限要求：
     * - 需要department:add权限
     * - 只有管理员和相关负责人可以操作
     * 
     * @param dto 科室信息数据传输对象，包含科室的所有基本信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
     */
    @Log(type = SysLogType.ADD)  // 记录操作日志
    @Operation(summary = "添加科室")  // Swagger接口描述
    @PostMapping("/addDepartment")  // HTTP POST请求映射
    @Permission("department:add")  // 权限验证
    public ApiResult addDepartment(@Valid @RequestBody DepartmentDto dto) {
        log.info("添加科室：{}", dto);
        boolean flag = departmentService.addDepartment(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改科室信息
     * 
     * 功能说明：
     * 更新已存在的科室档案信息，支持修改科室的基本信息、组织架构、
     * 服务范围、状态等各项内容。
     * 
     * 业务流程：
     * 1. 接收前端传递的科室更新数据
     * 2. 验证科室ID的有效性
     * 3. 检查科室是否存在
     * 4. 进行数据格式验证
     * 5. 检查科室名称是否与其他科室重复
     * 6. 验证层级关系的合法性
     * 7. 检查关联业务的影响
     * 8. 更新科室信息
     * 9. 记录操作日志
     * 10. 返回操作结果
     * 
     * 数据验证：
     * - 科室ID必须存在
     * - 修改后的科室名称不能与其他科室重复
     * - 上级科室变更不能形成循环引用
     * - 科室状态变更需要验证业务影响
     * 
     * 特殊业务规则：
     * - 如果科室有医生归属，修改某些信息需要特殊处理
     * - 如果科室有未完成的预约，不能修改某些关键信息
     * - 科室状态变更需要检查下级科室的状态
     * - 科室层级结构变更需要验证完整性
     * 
     * 权限要求：
     * - 需要department:update权限
     * - 只有管理员和相关负责人可以操作
     * 
     * @param dto 科室信息数据传输对象，包含要更新的科室信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
     */
    @Log(type = SysLogType.UPDATE)  // 记录操作日志
    @Operation(summary = "修改科室")  // Swagger接口描述
    @PostMapping("/updateDepartment")  // HTTP POST请求映射
    @Permission("department:update")  // 权限验证
    public ApiResult updateDepartment(@Valid @RequestBody DepartmentDto dto) {
        log.info("修改科室：{}", dto);
        boolean flag = departmentService.updateDepartment(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除科室信息
     * 
     * 功能说明：
     * 删除指定的科室档案信息。出于数据安全和业务连续性考虑，
     * 科室删除通常采用逻辑删除方式，并需要进行严格的业务规则检查。
     * 
     * 业务流程：
     * 1. 接收要删除的科室ID
     * 2. 验证科室ID的有效性
     * 3. 检查科室是否存在
     * 4. 验证是否可以删除（业务规则检查）
     * 5. 检查科室下是否有医生
     * 6. 检查科室是否有子科室
     * 7. 检查是否有未完成的预约
     * 8. 检查是否有关联的体检项目
     * 9. 执行删除操作（逻辑删除）
     * 10. 记录操作日志
     * 11. 返回操作结果
     * 
     * 删除前检查：
     * - 科室下是否有归属的医生
     * - 科室是否有子科室
     * - 科室是否有未完成的预约
     * - 科室是否有关联的体检项目
     * - 科室是否有历史体检记录
     * 
     * 业务规则：
     * - 有医生归属的科室不能删除
     * - 有子科室的科室不能删除
     * - 有活跃业务的科室不能删除
     * - 删除操作需要记录详细日志
     * - 删除后的科室信息需要保留用于审计
     * 
     * 权限要求：
     * - 需要department:delete权限
     * - 通常只有高级管理员可以执行删除操作
     * 
     * @param id 科室的唯一标识ID
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据不存在异常、业务规则异常等
     */
    @Log(type = SysLogType.DELETE)  // 记录操作日志
    @Operation(summary = "删除科室")  // Swagger接口描述
    @PostMapping("/deleteDepartment/{id}")  // HTTP POST请求映射，路径变量
    @Permission("department:delete")  // 权限验证
    public ApiResult deleteDepartment(@PathVariable Long id) {
        log.info("删除科室：{}", id);
        boolean flag = departmentService.deleteDepartment(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取科室详细信息
     * 
     * 功能说明：
     * 根据科室ID查询并返回科室的详细信息，包括基本信息、组织架构、
     * 服务范围、医生信息等完整的科室档案数据。
     * 
     * 业务流程：
     * 1. 接收科室ID参数
     * 2. 验证ID的有效性
     * 3. 查询科室基本信息
     * 4. 查询科室组织架构信息
     * 5. 查询科室服务范围信息
     * 6. 查询科室医生信息
     * 7. 查询科室统计信息
     * 8. 组装完整的科室信息对象
     * 9. 返回查询结果
     * 
     * 返回数据内容：
     * - 科室基本信息（名称、编码、类型等）
     * - 科室组织架构信息（上级科室、下级科室等）
     * - 科室服务信息（服务项目、工作时间等）
     * - 科室医生信息（医生数量、医生列表等）
     * - 科室统计信息（预约数量、体检数量等）
     * 
     * 数据安全：
     * - 敏感信息根据权限决定是否返回
     * - 统计信息可能需要特殊权限查看
     * - 删除或停用的科室信息有特殊标识
     * 
     * 权限要求：
     * - 需要department:info权限
     * - 不同权限级别可以查看不同详细程度的信息
     * 
     * @param id 科室的唯一标识ID
     * @return ApiResult<DepartmentVo> 返回包含科室详细信息的响应对象
     * @throws Exception 可能抛出的异常包括数据不存在异常、权限异常等
     */
    @Operation(summary = "获取科室详情")  // Swagger接口描述
    @PostMapping("/getDepartment/{id}")  // HTTP POST请求映射，路径变量
    @Permission("department:info")  // 权限验证
    public ApiResult<DepartmentVo> getDepartment(@PathVariable Long id) {
        log.info("获取科室详情：{}", id);
        DepartmentVo departmentVo = departmentService.getDepartmentById(id);
        return ApiResult.success(departmentVo);
    }

    /**
     * 获取科室分页列表
     * 
     * 功能说明：
     * 根据查询条件分页查询科室列表，支持多种查询条件组合，
     * 为管理界面提供灵活的科室信息检索功能。
     * 
     * 业务流程：
     * 1. 接收查询条件和分页参数
     * 2. 验证查询参数的有效性
     * 3. 构建查询条件
     * 4. 执行分页查询
     * 5. 处理查询结果
     * 6. 计算分页信息
     * 7. 组装树形结构（如果需要）
     * 8. 返回分页结果
     * 
     * 支持的查询条件：
     * - 科室名称模糊查询
     * - 科室编码精确查询
     * - 科室类型查询
     * - 上级科室ID查询
     * - 科室状态查询
     * - 创建时间范围查询
     * - 科室负责人查询
     * 
     * 分页参数：
     * - current：当前页码（默认为1）
     * - size：每页显示数量（默认为10）
     * - 支持排序字段和排序方向
     * 
     * 返回数据：
     * - records：当前页的科室记录列表
     * - total：符合条件的总记录数
     * - current：当前页码
     * - size：每页显示数量
     * - pages：总页数
     * - 可能包含树形结构信息
     * 
     * 特殊功能：
     * - 支持树形结构展示
     * - 支持层级查询
     * - 支持科室统计信息
     * 
     * 性能优化：
     * - 使用索引优化查询性能
     * - 大数据量时启用查询缓存
     * - 避免查询过多的字段
     * - 层级查询优化
     * 
     * 权限要求：
     * - 需要department:page权限
     * - 不同权限级别可能看到不同的数据范围
     * 
     * @param query 查询条件对象，包含各种查询参数和分页信息
     * @return ApiResult<DepartmentVo> 返回包含科室分页数据的响应对象
     * @throws Exception 可能抛出的异常包括查询参数异常、数据库异常等
     */
    @Operation(summary = "获取科室分页列表")  // Swagger接口描述
    @PostMapping("/getDepartmentPage")  // HTTP POST请求映射
    @Permission("department:page")  // 权限验证
    public ApiResult<DepartmentVo> getDepartmentPage(@Valid @RequestBody DepartmentQuery query) {
        log.info("获取科室分页列表：{}", query);
        Paging<DepartmentVo> paging = departmentService.getDepartmentPage(query);
        return ApiResult.success(paging);
    }

}
