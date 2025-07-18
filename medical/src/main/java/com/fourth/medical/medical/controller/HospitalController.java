package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.HospitalDto;
import com.fourth.medical.medical.query.HospitalQuery;
import com.fourth.medical.medical.service.HospitalService;
import com.fourth.medical.medical.vo.HospitalVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 医院管理控制器
 * 
 * 功能说明：
 * 本控制器是医疗体检系统中医院管理的核心控制器，负责处理医院基本信息的全生命周期管理。
 * 医院是整个医疗体检系统的基础机构，承载着科室、医生、体检项目等各种医疗资源。
 * 该控制器提供了医院信息的完整管理功能，包括医院档案建立、信息维护、状态管理等。
 * 
 * 主要功能：
 * 1. 医院基础信息管理
 *    - 新增医院档案
 *    - 修改医院信息
 *    - 删除医院档案
 *    - 查询医院详情
 *    - 医院列表分页查询
 * 
 * 2. 医院状态管理
 *    - 医院运营状态控制
 *    - 医院服务状态管理
 *    - 医院认证状态维护
 *    - 医院营业状态监控
 * 
 * 3. 医院基本信息管理
 *    - 医院名称和编码管理
 *    - 医院地址和联系信息
 *    - 医院等级和类型设置
 *    - 医院介绍和描述信息
 * 
 * 4. 医院资质管理
 *    - 医院营业执照信息
 *    - 医院医疗机构许可证
 *    - 医院认证资质管理
 *    - 医院服务范围设置
 * 
 * 业务特点：
 * - 严格的权限控制，确保医院信息的安全性
 * - 完整的操作日志记录，便于审计和问题追踪
 * - 数据完整性验证，确保医院信息的准确性
 * - 支持复杂查询条件，提供灵活的检索功能
 * - 状态管理机制，支持医院运营状态动态调整
 * 
 * 权限体系：
 * - hospital:add - 新增医院权限
 * - hospital:update - 修改医院权限
 * - hospital:delete - 删除医院权限
 * - hospital:info - 查看医院信息权限
 * - hospital:page - 医院列表查询权限
 * 
 * 数据安全：
 * - 医院敏感信息访问控制
 * - 商业机密数据保护
 * - 操作权限严格验证
 * - 数据变更完整记录
 * 
 * 关联模块：
 * - 科室管理：医院下属科室信息
 * - 医生管理：医院医生资源信息
 * - 体检项目：医院提供的体检服务
 * - 预约系统：医院预约服务管理
 * 
 * 业务规则：
 * - 医院名称必须唯一
 * - 医院编码全系统唯一
 * - 有关联业务的医院不能直接删除
 * - 医院状态变更需要验证业务影响
 * 
 * @author fourth
 * @since 2025-07-09
 * @version 1.0
 */
@Slf4j  // 自动生成日志对象log
@RestController  // 标识这是一个REST控制器，返回JSON格式数据
@Tag(name = "医院")  // Swagger文档标签，用于API分组
@RequestMapping("/admin/hospital")  // 设置控制器的基础请求路径
public class HospitalController {

    /**
     * 医院服务接口
     * 提供医院信息的业务逻辑处理，包括增删改查和业务规则验证
     */
    @Autowired
    private HospitalService hospitalService;

    /**
     * 添加医院信息
     * 
     * 功能说明：
     * 新增医院档案信息，包括医院的基本信息、联系方式、资质信息等。
     * 该接口用于系统管理人员向系统中添加新的医院记录。
     * 
     * 业务流程：
     * 1. 接收前端传递的医院信息数据
     * 2. 进行数据验证和格式检查
     * 3. 检查医院名称和编码是否重复
     * 4. 验证医院地址和联系方式格式
     * 5. 验证医院等级和类型的有效性
     * 6. 保存医院信息到数据库
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * 数据验证：
     * - 医院名称不能为空且必须唯一
     * - 医院编码必须唯一
     * - 联系电话格式验证
     * - 电子邮箱格式验证
     * - 医院地址不能为空
     * - 医院等级必须在有效范围内
     * 
     * 业务规则：
     * - 医院名称全系统唯一
     * - 医院编码全系统唯一
     * - 新建医院默认状态为启用
     * - 医院等级必须符合国家标准
     * 
     * 权限要求：
     * - 需要hospital:add权限
     * - 只有系统管理员可以操作
     * 
     * @param dto 医院信息数据传输对象，包含医院的所有基本信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
     */
    @Log(type = SysLogType.ADD)  // 记录操作日志
    @Operation(summary = "添加医院")  // Swagger接口描述
    @PostMapping("/add")  // HTTP POST请求映射
    @Permission("hospital:add")  // 权限验证
    public ApiResult addHospital(@Valid @RequestBody HospitalDto dto) {
        log.info("添加医院：{}", dto);
        boolean flag = hospitalService.addHospital(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改医院信息
     * 
     * 功能说明：
     * 更新已存在的医院档案信息，支持修改医院的基本信息、联系方式、
     * 资质信息、状态等各项内容。
     * 
     * 业务流程：
     * 1. 接收前端传递的医院更新数据
     * 2. 验证医院ID的有效性
     * 3. 检查医院是否存在
     * 4. 进行数据格式验证
     * 5. 检查医院名称和编码是否与其他医院重复
     * 6. 验证修改权限和业务规则
     * 7. 检查关联业务的影响
     * 8. 更新医院信息
     * 9. 记录操作日志
     * 10. 返回操作结果
     * 
     * 数据验证：
     * - 医院ID必须存在
     * - 修改后的医院名称不能与其他医院重复
     * - 修改后的医院编码不能与其他医院重复
     * - 联系信息格式验证
     * - 地址信息完整性验证
     * 
     * 特殊业务规则：
     * - 如果医院有关联的科室，某些信息修改需要特殊处理
     * - 如果医院有未完成的预约，不能修改某些关键信息
     * - 医院状态变更需要检查相关业务影响
     * - 医院等级变更可能影响服务范围
     * 
     * 权限要求：
     * - 需要hospital:update权限
     * - 只有系统管理员可以操作
     * 
     * @param dto 医院信息数据传输对象，包含要更新的医院信息
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
     */
    @Log(type = SysLogType.UPDATE)  // 记录操作日志
    @Operation(summary = "修改医院")  // Swagger接口描述
    @PutMapping("/update")  // HTTP PUT请求映射
    @Permission("hospital:update")  // 权限验证
    public ApiResult updateHospital(@Valid @RequestBody HospitalDto dto) {
        log.info("修改医院：{}", dto);
        boolean flag = hospitalService.updateHospital(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除医院信息
     * 
     * 功能说明：
     * 删除指定的医院档案信息。出于数据安全和业务连续性考虑，
     * 医院删除通常采用逻辑删除方式，并需要进行严格的业务规则检查。
     * 
     * 业务流程：
     * 1. 接收要删除的医院ID
     * 2. 验证医院ID的有效性
     * 3. 检查医院是否存在
     * 4. 验证是否可以删除（业务规则检查）
     * 5. 检查医院下是否有科室
     * 6. 检查医院是否有医生
     * 7. 检查是否有未完成的预约
     * 8. 检查是否有关联的体检项目
     * 9. 执行删除操作（逻辑删除）
     * 10. 记录操作日志
     * 11. 返回操作结果
     * 
     * 删除前检查：
     * - 医院下是否有科室
     * - 医院是否有医生
     * - 医院是否有未完成的预约
     * - 医院是否有关联的体检项目
     * - 医院是否有历史体检记录
     * 
     * 业务规则：
     * - 有科室的医院不能删除
     * - 有医生的医院不能删除
     * - 有活跃业务的医院不能删除
     * - 删除操作需要记录详细日志
     * - 删除后的医院信息需要保留用于审计
     * 
     * 权限要求：
     * - 需要hospital:delete权限
     * - 通常只有超级管理员可以执行删除操作
     * 
     * @param id 医院的唯一标识ID
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据不存在异常、业务规则异常等
     */
    @Log(type = SysLogType.DELETE)  // 记录操作日志
    @Operation(summary = "删除医院")  // Swagger接口描述
    @DeleteMapping("/delete/{id}")  // HTTP DELETE请求映射，路径变量
    @Permission("hospital:delete")  // 权限验证
    public ApiResult deleteHospital(@PathVariable Long id) {
        log.info("删除医院：{}", id);
        boolean flag = hospitalService.deleteHospital(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取医院详细信息
     * 
     * 功能说明：
     * 根据医院ID查询并返回医院的详细信息，包括基本信息、联系方式、
     * 资质信息、统计信息等完整的医院档案数据。
     * 
     * 业务流程：
     * 1. 接收医院ID参数
     * 2. 验证ID的有效性
     * 3. 查询医院基本信息
     * 4. 查询医院联系信息
     * 5. 查询医院资质信息
     * 6. 查询医院统计信息（如科室数量、医生数量等）
     * 7. 组装完整的医院信息对象
     * 8. 返回查询结果
     * 
     * 返回数据内容：
     * - 医院基本信息（名称、编码、类型、等级等）
     * - 医院联系信息（地址、电话、邮箱等）
     * - 医院资质信息（营业执照、许可证等）
     * - 医院状态信息（运营状态、服务状态等）
     * - 医院统计信息（科室数量、医生数量等）
     * 
     * 数据安全：
     * - 敏感信息根据权限决定是否返回
     * - 商业机密信息需要特殊权限查看
     * - 删除或停用的医院信息有特殊标识
     * 
     * 权限要求：
     * - 需要hospital:info权限
     * - 不同权限级别可以查看不同详细程度的信息
     * 
     * @param id 医院的唯一标识ID
     * @return ApiResult<HospitalVo> 返回包含医院详细信息的响应对象
     * @throws Exception 可能抛出的异常包括数据不存在异常、权限异常等
     */
    @Operation(summary = "获取医院详情")  // Swagger接口描述
    @GetMapping("/detail/{id}")  // HTTP GET请求映射，路径变量
    @Permission("hospital:info")  // 权限验证
    public ApiResult<HospitalVo> getHospital(@PathVariable Long id) {
        log.info("获取医院详情：{}", id);
        HospitalVo hospitalVo = hospitalService.getHospitalById(id);
        return ApiResult.success(hospitalVo);
    }

    /**
     * 获取医院分页列表
     * 
     * 功能说明：
     * 根据查询条件分页查询医院列表，支持多种查询条件组合，
     * 为管理界面提供灵活的医院信息检索功能。
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
     * - 医院名称模糊查询
     * - 医院编码精确查询
     * - 医院类型查询
     * - 医院等级查询
     * - 医院状态查询
     * - 创建时间范围查询
     * - 医院地址查询
     * 
     * 分页参数：
     * - current：当前页码（默认为1）
     * - size：每页显示数量（默认为10）
     * - 支持排序字段和排序方向
     * 
     * 返回数据：
     * - records：当前页的医院记录列表
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
     * - 需要hospital:page权限
     * - 不同权限级别可能看到不同的数据范围
     * 
     * @param query 查询条件对象，包含各种查询参数和分页信息
     * @return ApiResult<HospitalVo> 返回包含医院分页数据的响应对象
     * @throws Exception 可能抛出的异常包括查询参数异常、数据库异常等
     */
    @Operation(summary = "获取医院列表")  // Swagger接口描述
    @GetMapping("/list")  // HTTP GET请求映射
    @Permission("hospital:page")  // 权限验证
    public ApiResult<HospitalVo> getHospitalList(@Valid HospitalQuery query) {
        log.info("获取医院列表：{}", query);
        Paging<HospitalVo> paging = hospitalService.getHospitalPage(query);
        return ApiResult.success(paging);
    }

    /**
     * 更新医院状态
     * 
     * 功能说明：
     * 快速更新医院的运营状态，支持启用/禁用医院的服务。
     * 该接口主要用于医院运营状态的快速切换。
     * 
     * 业务流程：
     * 1. 接收医院ID和状态参数
     * 2. 验证医院ID的有效性
     * 3. 检查医院是否存在
     * 4. 验证状态变更权限
     * 5. 检查状态变更的业务影响
     * 6. 更新医院状态
     * 7. 记录操作日志
     * 8. 返回操作结果
     * 
     * 状态说明：
     * - true：启用医院服务
     * - false：禁用医院服务
     * 
     * 业务规则：
     * - 禁用医院时需要检查是否有未完成的预约
     * - 禁用医院时需要通知相关医生和患者
     * - 状态变更需要记录详细日志
     * - 状态变更可能影响科室和医生的服务状态
     * 
     * 权限要求：
     * - 需要hospital:update权限
     * - 只有系统管理员可以操作
     * 
     * @param id 医院的唯一标识ID
     * @param status 医院状态，true为启用，false为禁用
     * @return ApiResult 返回操作结果，成功返回true，失败返回false
     * @throws Exception 可能抛出的异常包括数据验证异常、业务规则异常等
     */
    @Log(type = SysLogType.UPDATE)  // 记录操作日志
    @Operation(summary = "更新医院状态")  // Swagger接口描述
    @PutMapping("/status")  // HTTP PUT请求映射
    @Permission("hospital:update")  // 权限验证
    public ApiResult updateHospitalStatus(@RequestParam Long id, @RequestParam Boolean status) {
        log.info("更新医院状态：id={}, status={}", id, status);
        HospitalDto dto = new HospitalDto();
        dto.setId(id);
        dto.setStatus(status);
        boolean flag = hospitalService.updateHospital(dto);
        return ApiResult.result(flag);
    }

}
