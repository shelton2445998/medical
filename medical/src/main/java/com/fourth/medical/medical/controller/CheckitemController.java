package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.CheckitemDto;
import com.fourth.medical.medical.query.CheckitemQuery;
import com.fourth.medical.medical.service.CheckitemService;
import com.fourth.medical.medical.vo.CheckitemVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 体检项目管理控制器
 * 
 * 功能说明：
 * 本控制器负责管理体检系统中的各种体检项目，包括项目的基本信息管理、
 * 项目分类管理、项目状态控制等功能。体检项目是构成体检套餐的基本单元，
 * 也是医院提供体检服务的核心内容。
 * 
 * 主要功能：
 * 1. 体检项目基础管理 - 项目的增删改查
 * 2. 项目分类管理 - 按照医学分类组织项目
 * 3. 项目状态管理 - 控制项目的可用性
 * 4. 项目定价管理 - 设置项目的收费标准
 * 5. 项目关联管理 - 管理项目与套餐、科室的关系
 * 
 * 业务特点：
 * - 支持多种体检项目类型（血液检查、影像检查、功能检查等）
 * - 灵活的项目分类和标签管理
 * - 完整的项目生命周期管理
 * - 严格的权限控制和操作日志记录
 * 
 * @author fourth
 * @since 2025-07-09
 * @version 1.0
 */
@Slf4j  // 自动生成日志对象
@RestController  // REST控制器注解
@Tag(name = "检查项")  // Swagger文档标签
@RequestMapping("/admin/check-item")  // 请求路径映射
public class CheckitemController {

    /**
     * 体检项目服务接口
     * 提供体检项目的业务逻辑处理功能
     */
    @Autowired
    private CheckitemService checkitemService;

    /**
     * 添加检查项
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加检查项")
    @PostMapping("/add")
    @Permission("checkitem:add")
    public ApiResult addCheckitem(@Valid @RequestBody CheckitemDto dto) {
        log.info("添加检查项：{}", dto);
        boolean flag = checkitemService.addCheckitem(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改检查项
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改检查项")
    @PutMapping("/update")
    @Permission("checkitem:update")
    public ApiResult updateCheckitem(@Valid @RequestBody CheckitemDto dto) {
        log.info("修改检查项：{}", dto);
        boolean flag = checkitemService.updateCheckitem(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除检查项
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除检查项")
    @DeleteMapping("/delete/{itemId}")
    @Permission("checkitem:delete")
    public ApiResult deleteCheckitem(@PathVariable("itemId") Long id) {
        log.info("删除检查项：{}", id);
        boolean flag = checkitemService.deleteCheckitem(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取检查项详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取检查项详情")
    @GetMapping("/detail/{itemId}")
    @Permission("checkitem:info")
    public ApiResult<CheckitemVo> getCheckitem(@PathVariable("itemId") Long id) {
        log.info("获取检查项详情：{}", id);
        CheckitemVo checkitemVo = checkitemService.getCheckitemById(id);
        return ApiResult.success(checkitemVo);
    }

    /**
     * 获取检查项列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取检查项列表")
    @GetMapping("/list")
    @Permission("checkitem:page")
    public ApiResult<CheckitemVo> getCheckitemList(@Valid CheckitemQuery query) {
        log.info("获取检查项列表：{}", query);
        Paging<CheckitemVo> paging = checkitemService.getCheckitemPage(query);
        return ApiResult.success(paging);
    }

    /**
     * 更新检查项状态
     *
     * @param id
     * @param status
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "更新检查项状态")
    @PutMapping("/status")
    @Permission("checkitem:update")
    public ApiResult updateCheckitemStatus(@RequestParam Long id, @RequestParam Boolean status) {
        log.info("更新检查项状态：id={}, status={}", id, status);
        CheckitemDto dto = new CheckitemDto();
        dto.setId(id);
        dto.setStatus(status);
        boolean flag = checkitemService.updateCheckitem(dto);
        return ApiResult.result(flag);
    }

}
