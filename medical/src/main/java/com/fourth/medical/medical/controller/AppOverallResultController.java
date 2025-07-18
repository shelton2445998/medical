package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppOverallResultQuery;
import com.fourth.medical.medical.service.OverallResultService;
import com.fourth.medical.medical.vo.AppOverallResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App总检结论 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>为移动端APP提供总检结论查询服务，包括体检总结、健康建议、复查建议等</li>
 *   <li>支持总检结论的详情查看和分页查询功能</li>
 *   <li>为移动端用户提供便捷的体检结果查看服务</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>移动端适配：专门为移动端APP设计的接口</li>
 *   <li>总检结论查询：支持总检结论的详情和列表查询</li>
 *   <li>用户友好：提供简洁易用的移动端数据格式</li>
 *   <li>性能优化：针对移动端网络环境进行性能优化</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>用户登录：移动端用户登录后获取总检结论列表</li>
 *   <li>结论查看：用户查看具体的总检结论详情</li>
 *   <li>分页查询：支持分页加载总检结论数据</li>
 *   <li>数据同步：与后端总检结论数据保持同步</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>移动端用户认证：通过用户登录状态进行身份验证</li>
 *   <li>数据权限：用户只能查看自己的总检结论数据</li>
 *   <li>接口安全：通过Token验证确保接口调用安全</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>用户隔离：确保用户只能访问自己的数据</li>
 *   <li>数据传输：使用HTTPS确保数据传输安全</li>
 *   <li>Token验证：通过JWT Token进行身份验证</li>
 *   <li>数据脱敏：对敏感信息进行适当脱敏处理</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>推送通知：可扩展支持总检结论完成后的推送通知</li>
 *   <li>健康建议：可扩展支持个性化的健康建议推送</li>
 *   <li>复查提醒：可扩展支持复查时间提醒功能</li>
 *   <li>数据导出：可扩展支持总检结论的PDF导出功能</li>
 * </ul>
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App总检结论")
@RequestMapping("/app/overallResult")
public class AppOverallResultController {

    @Autowired
    private OverallResultService overallResultService;

    /**
     * 获取App总检结论详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App总检结论详情")
    @PostMapping("/getAppOverallResult/{id}")
    public ApiResult<AppOverallResultVo> getAppOverallResult(@PathVariable Long id) {
        log.info("获取App总检结论详情：{}", id);
        AppOverallResultVo appOverallResultVo = overallResultService.getAppOverallResultById(id);
        return ApiResult.success(appOverallResultVo);
    }

    /**
     * 获取App总检结论分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App总检结论分页列表")
    @PostMapping("/getAppOverallResultPage")
    public ApiResult<AppOverallResultVo> getAppOverallResultPage(@Valid @RequestBody AppOverallResultQuery query) {
        log.info("获取App总检结论分页列表：{}", query);
        Paging<AppOverallResultVo> paging = overallResultService.getAppOverallResultPage(query);
        return ApiResult.success(paging);
    }

}
