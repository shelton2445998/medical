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
