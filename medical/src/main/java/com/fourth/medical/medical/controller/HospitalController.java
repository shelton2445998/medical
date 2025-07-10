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
 * 医院 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "医院")
@RequestMapping("/admin/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    /**
     * 添加医院
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加医院")
    @PostMapping("/add")
    @Permission("hospital:add")
    public ApiResult addHospital(@Valid @RequestBody HospitalDto dto) {
        log.info("添加医院：{}", dto);
        boolean flag = hospitalService.addHospital(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改医院
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改医院")
    @PutMapping("/update")
    @Permission("hospital:update")
    public ApiResult updateHospital(@Valid @RequestBody HospitalDto dto) {
        log.info("修改医院：{}", dto);
        boolean flag = hospitalService.updateHospital(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除医院
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除医院")
    @DeleteMapping("/delete/{id}")
    @Permission("hospital:delete")
    public ApiResult deleteHospital(@PathVariable Long id) {
        log.info("删除医院：{}", id);
        boolean flag = hospitalService.deleteHospital(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取医院详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取医院详情")
    @GetMapping("/detail/{id}")
    @Permission("hospital:info")
    public ApiResult<HospitalVo> getHospital(@PathVariable Long id) {
        log.info("获取医院详情：{}", id);
        HospitalVo hospitalVo = hospitalService.getHospitalById(id);
        return ApiResult.success(hospitalVo);
    }

    /**
     * 获取医院列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取医院列表")
    @GetMapping("/list")
    @Permission("hospital:page")
    public ApiResult<HospitalVo> getHospitalList(@Valid HospitalQuery query) {
        log.info("获取医院列表：{}", query);
        Paging<HospitalVo> paging = hospitalService.getHospitalPage(query);
        return ApiResult.success(paging);
    }

    /**
     * 更新医院状态
     *
     * @param id
     * @param status
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "更新医院状态")
    @PutMapping("/status")
    @Permission("hospital:update")
    public ApiResult updateHospitalStatus(@RequestParam Long id, @RequestParam Boolean status) {
        log.info("更新医院状态：id={}, status={}", id, status);
        HospitalDto dto = new HospitalDto();
        dto.setId(id);
        dto.setStatus(status);
        boolean flag = hospitalService.updateHospital(dto);
        return ApiResult.result(flag);
    }

}
