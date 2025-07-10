package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.CheckitemDetailDto;
import com.fourth.medical.medical.query.CheckitemDetailQuery;
import com.fourth.medical.medical.service.CheckitemDetailService;
import com.fourth.medical.medical.vo.CheckitemDetailVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 检查项明细 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "检查项明细")
@RequestMapping("/admin/checkitemDetail")
public class CheckitemDetailController {

    @Autowired
    private CheckitemDetailService checkitemDetailService;

    /**
     * 添加检查项明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加检查项明细")
    @PostMapping("/addCheckitemDetail")
    @Permission("checkitem:detail:add")
    public ApiResult addCheckitemDetail(@Valid @RequestBody CheckitemDetailDto dto) {
        log.info("添加检查项明细：{}", dto);
        boolean flag = checkitemDetailService.addCheckitemDetail(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改检查项明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改检查项明细")
    @PostMapping("/updateCheckitemDetail")
    @Permission("checkitem:detail:update")
    public ApiResult updateCheckitemDetail(@Valid @RequestBody CheckitemDetailDto dto) {
        log.info("修改检查项明细：{}", dto);
        boolean flag = checkitemDetailService.updateCheckitemDetail(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除检查项明细
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除检查项明细")
    @PostMapping("/deleteCheckitemDetail/{id}")
    @Permission("checkitem:detail:delete")
    public ApiResult deleteCheckitemDetail(@PathVariable Long id) {
        log.info("删除检查项明细：{}", id);
        boolean flag = checkitemDetailService.deleteCheckitemDetail(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取检查项明细详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取检查项明细详情")
    @PostMapping("/getCheckitemDetail/{id}")
    @Permission("checkitem:detail:info")
    public ApiResult<CheckitemDetailVo> getCheckitemDetail(@PathVariable Long id) {
        log.info("获取检查项明细详情：{}", id);
        CheckitemDetailVo checkitemDetailVo = checkitemDetailService.getCheckitemDetailById(id);
        return ApiResult.success(checkitemDetailVo);
    }

    /**
     * 获取检查项明细分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取检查项明细分页列表")
    @PostMapping("/getCheckitemDetailPage")
    @Permission("checkitem:detail:page")
    public ApiResult<CheckitemDetailVo> getCheckitemDetailPage(@Valid @RequestBody CheckitemDetailQuery query) {
        log.info("获取检查项明细分页列表：{}", query);
        Paging<CheckitemDetailVo> paging = checkitemDetailService.getCheckitemDetailPage(query);
        return ApiResult.success(paging);
    }

}
