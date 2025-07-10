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
 * 体检套餐 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "体检套餐")
@RequestMapping("/admin/package")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 添加体检套餐
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加体检套餐")
    @PostMapping("/add")
    @Permission("setmeal:add")
    public ApiResult addSetmeal(@Valid @RequestBody SetmealDto dto) {
        log.info("添加体检套餐：{}", dto);
        boolean flag = setmealService.addSetmeal(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改体检套餐
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改体检套餐")
    @PutMapping("/update")
    @Permission("setmeal:update")
    public ApiResult updateSetmeal(@Valid @RequestBody SetmealDto dto) {
        log.info("修改体检套餐：{}", dto);
        boolean flag = setmealService.updateSetmeal(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除体检套餐
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除体检套餐")
    @DeleteMapping("/delete/{packageId}")
    @Permission("setmeal:delete")
    public ApiResult deleteSetmeal(@PathVariable("packageId") Long id) {
        log.info("删除体检套餐：{}", id);
        boolean flag = setmealService.deleteSetmeal(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取体检套餐详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检套餐详情")
    @GetMapping("/detail/{packageId}")
    @Permission("setmeal:info")
    public ApiResult<SetmealVo> getSetmeal(@PathVariable("packageId") Long id) {
        log.info("获取体检套餐详情：{}", id);
        SetmealVo setmealVo = setmealService.getSetmealById(id);
        return ApiResult.success(setmealVo);
    }

    /**
     * 获取体检套餐列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检套餐列表")
    @GetMapping("/list")
    @Permission("setmeal:page")
    public ApiResult<SetmealVo> getSetmealList(@Valid SetmealQuery query) {
        log.info("获取体检套餐列表：{}", query);
        Paging<SetmealVo> paging = setmealService.getSetmealPage(query);
        return ApiResult.success(paging);
    }

    /**
     * 更新体检套餐状态
     *
     * @param id
     * @param status
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "更新体检套餐状态")
    @PutMapping("/status")
    @Permission("setmeal:update")
    public ApiResult updateSetmealStatus(@RequestParam Long id, @RequestParam Boolean status) {
        log.info("更新体检套餐状态：id={}, status={}", id, status);
        SetmealDto dto = new SetmealDto();
        dto.setId(id);
        dto.setStatus(status);
        boolean flag = setmealService.updateSetmeal(dto);
        return ApiResult.result(flag);
    }

}
