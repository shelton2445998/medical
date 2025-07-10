package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppFamilyMemberQuery;
import com.fourth.medical.medical.service.FamilyMemberService;
import com.fourth.medical.medical.vo.AppFamilyMemberVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App家庭成员 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App家庭成员")
@RequestMapping("/app/familyMember")
public class AppFamilyMemberController {

    @Autowired
    private FamilyMemberService familyMemberService;

    /**
     * 获取App家庭成员详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App家庭成员详情")
    @PostMapping("/getAppFamilyMember/{id}")
    public ApiResult<AppFamilyMemberVo> getAppFamilyMember(@PathVariable Long id) {
        log.info("获取App家庭成员详情：{}", id);
        AppFamilyMemberVo appFamilyMemberVo = familyMemberService.getAppFamilyMemberById(id);
        return ApiResult.success(appFamilyMemberVo);
    }

    /**
     * 获取App家庭成员分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App家庭成员分页列表")
    @PostMapping("/getAppFamilyMemberPage")
    public ApiResult<AppFamilyMemberVo> getAppFamilyMemberPage(@Valid @RequestBody AppFamilyMemberQuery query) {
        log.info("获取App家庭成员分页列表：{}", query);
        Paging<AppFamilyMemberVo> paging = familyMemberService.getAppFamilyMemberPage(query);
        return ApiResult.success(paging);
    }

}
