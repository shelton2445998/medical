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
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>管理用户的家庭成员信息，包括家庭成员的基本信息、关系、联系方式等</li>
 *   <li>支持家庭成员的增删改查操作，为用户提供家庭成员管理服务</li>
 *   <li>提供家庭成员的分页查询和详情查看功能</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>家庭成员管理：支持家庭成员信息的创建、编辑、删除和查询</li>
 *   <li>关系管理：支持家庭成员关系的定义和管理</li>
 *   <li>权限控制：基于角色的访问控制，确保数据安全</li>
 *   <li>操作日志：记录所有关键操作，便于审计和追踪</li>
 *   <li>数据验证：对输入数据进行严格验证，确保数据完整性</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>成员添加：用户添加家庭成员，填写基本信息、关系等</li>
 *   <li>信息维护：根据实际情况更新家庭成员信息</li>
 *   <li>成员查询：支持按条件查询和分页展示家庭成员列表</li>
 *   <li>成员删除：删除不再需要的家庭成员信息</li>
 *   <li>关系管理：维护家庭成员之间的关系信息</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>family:member:add - 添加家庭成员权限</li>
 *   <li>family:member:update - 修改家庭成员权限</li>
 *   <li>family:member:delete - 删除家庭成员权限</li>
 *   <li>family:member:info - 查看家庭成员详情权限</li>
 *   <li>family:member:page - 查看家庭成员列表权限</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>输入验证：使用@Valid注解进行参数验证</li>
 *   <li>SQL注入防护：使用MyBatis参数化查询</li>
 *   <li>权限校验：每个接口都有对应的权限控制</li>
 *   <li>操作日志：记录所有关键操作，便于安全审计</li>
 *   <li>隐私保护：确保家庭成员隐私信息的安全性</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>健康档案：可扩展支持家庭成员健康档案管理</li>
 *   <li>预约代理：可扩展支持为家庭成员代理预约体检</li>
 *   <li>报告共享：可扩展支持家庭成员体检报告共享</li>
 *   <li>紧急联系人：可扩展支持紧急联系人管理功能</li>
 * </ul>
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
