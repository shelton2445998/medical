package com.fourth.medical.system.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.system.dto.SysDictTypeDto;
import com.fourth.medical.system.query.SysDictTypeQuery;
import com.fourth.medical.system.service.SysDictTypeService;
import com.fourth.medical.system.vo.SysDictTypeVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 字典类型 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>管理系统字典类型，包括字典类型的创建、修改、删除和查询功能</li>
 *   <li>支持字典类型的增删改查操作，为系统提供统一的数据字典管理</li>
 *   <li>提供字典类型的列表查询和详情查看功能</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>字典类型管理：支持字典类型的创建、编辑、删除和查询</li>
 *   <li>类型分类：支持不同类型的字典分类管理</li>
 *   <li>状态管理：支持字典类型的启用/禁用状态控制</li>
 *   <li>权限控制：基于角色的访问控制，确保数据安全</li>
 *   <li>操作日志：记录所有关键操作，便于审计和追踪</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>类型创建：管理员创建新的字典类型，设置类型名称和描述</li>
 *   <li>类型维护：根据业务需求更新字典类型信息</li>
 *   <li>类型查询：支持按条件查询和展示字典类型列表</li>
 *   <li>类型删除：删除不再使用的字典类型（需谨慎操作）</li>
 *   <li>字典关联：将字典类型与具体的字典数据关联</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>sys:dict:type:add - 添加字典类型权限</li>
 *   <li>sys:dict:type:update - 修改字典类型权限</li>
 *   <li>sys:dict:type:delete - 删除字典类型权限</li>
 *   <li>sys:dict:type:info - 查看字典类型详情权限</li>
 *   <li>sys:dict:type:list - 查看字典类型列表权限</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>输入验证：使用@Valid注解进行参数验证</li>
 *   <li>SQL注入防护：使用MyBatis参数化查询</li>
 *   <li>权限校验：每个接口都有对应的权限控制</li>
 *   <li>操作日志：记录所有关键操作，便于安全审计</li>
 *   <li>数据完整性：确保字典类型数据的准确性和完整性</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>字典缓存：可扩展支持字典数据的缓存机制</li>
 *   <li>批量操作：可扩展支持批量导入、导出字典类型</li>
 *   <li>版本控制：可扩展支持字典类型版本管理</li>
 *   <li>国际化：可扩展支持多语言字典类型管理</li>
 * </ul>
 *
 * @author geekidea
 * @since 2023-11-25
 */
@Slf4j
@RestController
@Tag(name = "字典类型")
@RequestMapping("/admin/sysDictType")
public class SysDictTypeController {

    @Autowired
    private SysDictTypeService sysDictTypeService;

    /**
     * 添加字典类型
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(value = "添加字典类型", type = SysLogType.ADD)
    @PostMapping("/addSysDictType")
    @Operation(summary = "添加字典类型")
    @Permission("sys:dict:type:add")
    public ApiResult addSysDictType(@Valid @RequestBody SysDictTypeDto dto) {
        boolean flag = sysDictTypeService.addSysDictType(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改字典类型
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(value = "修改字典类型", type = SysLogType.UPDATE)
    @PostMapping("/updateSysDictType")
    @Operation(summary = "修改字典类型")
    @Permission("sys:dict:type:update")
    public ApiResult updateSysDictType(@Valid @RequestBody SysDictTypeDto dto) {
        boolean flag = sysDictTypeService.updateSysDictType(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除字典类型
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(value = "删除字典类型", type = SysLogType.DELETE)
    @PostMapping("/deleteSysDictType/{id}")
    @Operation(summary = "删除字典类型")
    @Permission("sys:dict:type:delete")
    public ApiResult deleteSysDictType(@PathVariable Long id) {
        boolean flag = sysDictTypeService.deleteSysDictType(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取字典类型详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysDictType/{id}")
    @Operation(summary = "字典类型详情")
    @Permission("sys:dict:type:info")
    public ApiResult<SysDictTypeVo> getSysDictType(@PathVariable Long id) {
        SysDictTypeVo sysDictTypeVo = sysDictTypeService.getSysDictTypeById(id);
        return ApiResult.success(sysDictTypeVo);
    }

    /**
     * 字典类型列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysDictTypeList")
    @Operation(summary = "字典类型列表")
    @Permission("sys:dict:type:list")
    public ApiResult<SysDictTypeVo> getSysDictTypeList(@Valid @RequestBody SysDictTypeQuery query) {
        List<SysDictTypeVo> list = sysDictTypeService.getSysDictTypeList(query);
        return ApiResult.success(list);
    }

}
