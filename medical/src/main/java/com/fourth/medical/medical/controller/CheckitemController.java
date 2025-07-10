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
 * 检查项 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "检查项")
@RequestMapping("/admin/check-item")
public class CheckitemController {

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
