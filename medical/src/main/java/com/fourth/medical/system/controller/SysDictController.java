package com.fourth.medical.system.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.system.dto.SysDictDto;
import com.fourth.medical.system.query.SysDictQuery;
import com.fourth.medical.system.service.SysDictService;
import com.fourth.medical.system.vo.SysDictVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 字典数据 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>管理系统字典数据，包括字典数据的创建、修改、删除和查询功能</li>
 *   <li>支持字典数据的增删改查操作，为系统提供统一的数据字典管理</li>
 *   <li>提供字典数据的分页查询和详情查看功能</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>字典数据管理：支持字典数据的创建、编辑、删除和查询</li>
 *   <li>数据分类：支持不同类型的字典数据分类管理</li>
 *   <li>状态管理：支持字典数据的启用/禁用状态控制</li>
 *   <li>权限控制：基于角色的访问控制，确保数据安全</li>
 *   <li>操作日志：记录所有关键操作，便于审计和追踪</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>数据创建：管理员创建新的字典数据，设置数据名称、值和描述</li>
 *   <li>数据维护：根据业务需求更新字典数据信息</li>
 *   <li>数据查询：支持按条件查询和分页展示字典数据列表</li>
 *   <li>数据删除：删除不再使用的字典数据（需谨慎操作）</li>
 *   <li>类型关联：将字典数据与具体的字典类型关联</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>sys:dict:add - 添加字典数据权限</li>
 *   <li>sys:dict:update - 修改字典数据权限</li>
 *   <li>sys:dict:delete - 删除字典数据权限</li>
 *   <li>sys:dict:info - 查看字典数据详情权限</li>
 *   <li>sys:dict:page - 查看字典数据列表权限</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>输入验证：使用@Valid注解进行参数验证</li>
 *   <li>SQL注入防护：使用MyBatis参数化查询</li>
 *   <li>权限校验：每个接口都有对应的权限控制</li>
 *   <li>操作日志：记录所有关键操作，便于安全审计</li>
 *   <li>数据完整性：确保字典数据的准确性和完整性</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>字典缓存：可扩展支持字典数据的缓存机制</li>
 *   <li>批量操作：可扩展支持批量导入、导出字典数据</li>
 *   <li>版本控制：可扩展支持字典数据版本管理</li>
 *   <li>国际化：可扩展支持多语言字典数据管理</li>
 * </ul>
 *
 * @author geekidea
 * @since 2023-11-25
 */
@Slf4j
@RestController
@Tag(name = "字典数据")
@RequestMapping("/admin/sysDict")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    /**
     * 添加字典数据
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(value = "添加字典数据", type = SysLogType.ADD)
    @PostMapping("/addSysDict")
    @Operation(summary = "添加字典数据")
    @Permission("sys:dict:add")
    public ApiResult addSysDict(@Valid @RequestBody SysDictDto dto) {
        boolean flag = sysDictService.addSysDict(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改字典数据
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(value = "修改字典数据", type = SysLogType.UPDATE)
    @PostMapping("/updateSysDict")
    @Operation(summary = "修改字典数据")
    @Permission("sys:dict:update")
    public ApiResult updateSysDict(@Valid @RequestBody SysDictDto dto) {
        boolean flag = sysDictService.updateSysDict(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除字典数据
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(value = "删除字典数据", type = SysLogType.DELETE)
    @PostMapping("/deleteSysDict/{id}")
    @Operation(summary = "删除字典数据")
    @Permission("sys:dict:delete")
    public ApiResult deleteSysDict(@PathVariable Long id) {
        boolean flag = sysDictService.deleteSysDict(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取字典数据详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysDict/{id}")
    @Operation(summary = "字典数据详情")
    @Permission("sys:dict:info")
    public ApiResult<SysDictVo> getSysDict(@PathVariable Long id) {
        SysDictVo sysDictVo = sysDictService.getSysDictById(id);
        return ApiResult.success(sysDictVo);
    }

    /**
     * 字典数据分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysDictPage")
    @Operation(summary = "字典数据分页列表")
    @Permission("sys:dict:page")
    public ApiResult<SysDictVo> getSysDictPage(@Valid @RequestBody SysDictQuery query) {
        Paging<SysDictVo> paging = sysDictService.getSysDictPage(query);
        return ApiResult.success(paging);
    }

}
