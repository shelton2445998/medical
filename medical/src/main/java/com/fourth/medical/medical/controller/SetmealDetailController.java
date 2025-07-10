package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.SetmealDetailDto;
import com.fourth.medical.medical.query.SetmealDetailQuery;
import com.fourth.medical.medical.service.SetmealDetailService;
import com.fourth.medical.medical.vo.SetmealDetailVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 体检套餐明细 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "体检套餐明细")
@RequestMapping("/admin/setmealDetail")
public class SetmealDetailController {

    @Autowired
    private SetmealDetailService setmealDetailService;

    /**
     * 添加体检套餐明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加体检套餐明细")
    @PostMapping("/addSetmealDetail")
    @Permission("setmeal:detail:add")
    public ApiResult addSetmealDetail(@Valid @RequestBody SetmealDetailDto dto) {
        log.info("添加体检套餐明细：{}", dto);
        boolean flag = setmealDetailService.addSetmealDetail(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改体检套餐明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改体检套餐明细")
    @PostMapping("/updateSetmealDetail")
    @Permission("setmeal:detail:update")
    public ApiResult updateSetmealDetail(@Valid @RequestBody SetmealDetailDto dto) {
        log.info("修改体检套餐明细：{}", dto);
        boolean flag = setmealDetailService.updateSetmealDetail(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除体检套餐明细
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除体检套餐明细")
    @PostMapping("/deleteSetmealDetail/{id}")
    @Permission("setmeal:detail:delete")
    public ApiResult deleteSetmealDetail(@PathVariable Long id) {
        log.info("删除体检套餐明细：{}", id);
        boolean flag = setmealDetailService.deleteSetmealDetail(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取体检套餐明细详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检套餐明细详情")
    @PostMapping("/getSetmealDetail/{id}")
    @Permission("setmeal:detail:info")
    public ApiResult<SetmealDetailVo> getSetmealDetail(@PathVariable Long id) {
        log.info("获取体检套餐明细详情：{}", id);
        SetmealDetailVo setmealDetailVo = setmealDetailService.getSetmealDetailById(id);
        return ApiResult.success(setmealDetailVo);
    }

    /**
     * 获取体检套餐明细分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检套餐明细分页列表")
    @PostMapping("/getSetmealDetailPage")
    @Permission("setmeal:detail:page")
    public ApiResult<SetmealDetailVo> getSetmealDetailPage(@Valid @RequestBody SetmealDetailQuery query) {
        log.info("获取体检套餐明细分页列表：{}", query);
        Paging<SetmealDetailVo> paging = setmealDetailService.getSetmealDetailPage(query);
        return ApiResult.success(paging);
    }

}
