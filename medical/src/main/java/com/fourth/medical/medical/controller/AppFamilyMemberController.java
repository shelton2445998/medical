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
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>为移动端APP提供家庭成员管理服务，包括家庭成员详情、家庭成员列表、家庭成员管理等</li>
 *   <li>支持家庭成员的详情查看和分页查询功能</li>
 *   <li>为移动端用户提供便捷的家庭成员管理服务</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>移动端适配：专门为移动端APP设计的接口</li>
 *   <li>家庭成员查询：支持家庭成员的详情和列表查询</li>
 *   <li>关系管理：支持家庭成员关系的显示和管理</li>
 *   <li>用户友好：提供简洁易用的移动端数据格式</li>
 *   <li>隐私保护：确保家庭成员信息的隐私安全</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>用户登录：移动端用户登录后获取家庭成员列表</li>
 *   <li>成员查看：用户查看具体的家庭成员详情</li>
 *   <li>成员管理：用户管理自己的家庭成员信息</li>
 *   <li>分页查询：支持分页加载家庭成员数据</li>
 *   <li>关系维护：维护家庭成员之间的关系信息</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>移动端用户认证：通过用户登录状态进行身份验证</li>
 *   <li>数据权限：用户只能查看自己的家庭成员数据</li>
 *   <li>接口安全：通过Token验证确保接口调用安全</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>用户隔离：确保用户只能访问自己的数据</li>
 *   <li>数据传输：使用HTTPS确保数据传输安全</li>
 *   <li>Token验证：通过JWT Token进行身份验证</li>
 *   <li>隐私保护：确保家庭成员隐私信息的安全性</li>
 *   <li>数据脱敏：对敏感信息进行适当脱敏处理</li>
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
