package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.FamilyMemberDto;
import com.fourth.medical.medical.query.FamilyMemberQuery;
import com.fourth.medical.medical.service.FamilyMemberService;
import com.fourth.medical.medical.vo.FamilyMemberVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 家庭成员 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "家庭成员")
@RequestMapping("/admin/familyMember")
public class FamilyMemberController {

    @Autowired
    private FamilyMemberService familyMemberService;

    /**
     * 添加家庭成员
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加家庭成员")
    @PostMapping("/addFamilyMember")
    @Permission("family:member:add")
    public ApiResult addFamilyMember(@Valid @RequestBody FamilyMemberDto dto) {
        log.info("添加家庭成员：{}", dto);
        boolean flag = familyMemberService.addFamilyMember(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改家庭成员
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改家庭成员")
    @PostMapping("/updateFamilyMember")
    @Permission("family:member:update")
    public ApiResult updateFamilyMember(@Valid @RequestBody FamilyMemberDto dto) {
        log.info("修改家庭成员：{}", dto);
        boolean flag = familyMemberService.updateFamilyMember(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除家庭成员
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除家庭成员")
    @PostMapping("/deleteFamilyMember/{id}")
    @Permission("family:member:delete")
    public ApiResult deleteFamilyMember(@PathVariable Long id) {
        log.info("删除家庭成员：{}", id);
        boolean flag = familyMemberService.deleteFamilyMember(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取家庭成员详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取家庭成员详情")
    @PostMapping("/getFamilyMember/{id}")
    @Permission("family:member:info")
    public ApiResult<FamilyMemberVo> getFamilyMember(@PathVariable Long id) {
        log.info("获取家庭成员详情：{}", id);
        FamilyMemberVo familyMemberVo = familyMemberService.getFamilyMemberById(id);
        return ApiResult.success(familyMemberVo);
    }

    /**
     * 获取家庭成员分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取家庭成员分页列表")
    @PostMapping("/getFamilyMemberPage")
    @Permission("family:member:page")
    public ApiResult<FamilyMemberVo> getFamilyMemberPage(@Valid @RequestBody FamilyMemberQuery query) {
        log.info("获取家庭成员分页列表：{}", query);
        Paging<FamilyMemberVo> paging = familyMemberService.getFamilyMemberPage(query);
        return ApiResult.success(paging);
    }

}
