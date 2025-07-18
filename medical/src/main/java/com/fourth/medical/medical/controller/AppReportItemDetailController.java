package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppReportItemDetailQuery;
import com.fourth.medical.medical.service.ReportItemDetailService;
import com.fourth.medical.medical.vo.AppReportItemDetailVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App体检报告检查项明细 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>为移动端APP提供体检报告检查项明细查询服务，包括具体的检查数值、异常标记、医生建议等</li>
 *   <li>支持检查项明细的详情查看和分页查询功能</li>
 *   <li>为移动端用户提供详细的检查结果查看服务</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>移动端适配：专门为移动端APP设计的接口</li>
 *   <li>检查项明细查询：支持检查项详细结果的查询</li>
 *   <li>异常标记显示：清晰显示检查结果的异常状态</li>
 *   <li>医生建议展示：展示医生对检查结果的建议和说明</li>
 *   <li>用户友好：提供简洁易用的移动端数据格式</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>用户登录：移动端用户登录后获取检查项明细列表</li>
 *   <li>明细查看：用户查看具体的检查项明细详情</li>
 *   <li>异常识别：系统自动标识异常的检查结果</li>
 *   <li>建议查看：用户查看医生对检查结果的建议</li>
 *   <li>分页查询：支持分页加载检查项明细数据</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>移动端用户认证：通过用户登录状态进行身份验证</li>
 *   <li>数据权限：用户只能查看自己的检查项明细数据</li>
 *   <li>接口安全：通过Token验证确保接口调用安全</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>用户隔离：确保用户只能访问自己的数据</li>
 *   <li>数据传输：使用HTTPS确保数据传输安全</li>
 *   <li>Token验证：通过JWT Token进行身份验证</li>
 *   <li>数据脱敏：对敏感信息进行适当脱敏处理</li>
 *   <li>结果准确性：确保检查结果的准确性和完整性</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>结果对比：可扩展支持历史检查结果对比功能</li>
 *   <li>趋势分析：可扩展支持检查结果趋势分析</li>
 *   <li>异常提醒：可扩展支持异常结果的智能提醒</li>
 *   <li>健康建议：可扩展支持基于检查结果的健康建议</li>
 * </ul>
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App体检报告检查项明细")
@RequestMapping("/app/reportItemDetail")
public class AppReportItemDetailController {

    @Autowired
    private ReportItemDetailService reportItemDetailService;

    /**
     * 获取App体检报告检查项明细详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App体检报告检查项明细详情")
    @PostMapping("/getAppReportItemDetail/{id}")
    public ApiResult<AppReportItemDetailVo> getAppReportItemDetail(@PathVariable Long id) {
        log.info("获取App体检报告检查项明细详情：{}", id);
        AppReportItemDetailVo appReportItemDetailVo = reportItemDetailService.getAppReportItemDetailById(id);
        return ApiResult.success(appReportItemDetailVo);
    }

    /**
     * 获取App体检报告检查项明细分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App体检报告检查项明细分页列表")
    @PostMapping("/getAppReportItemDetailPage")
    public ApiResult<AppReportItemDetailVo> getAppReportItemDetailPage(@Valid @RequestBody AppReportItemDetailQuery query) {
        log.info("获取App体检报告检查项明细分页列表：{}", query);
        Paging<AppReportItemDetailVo> paging = reportItemDetailService.getAppReportItemDetailPage(query);
        return ApiResult.success(paging);
    }

}
