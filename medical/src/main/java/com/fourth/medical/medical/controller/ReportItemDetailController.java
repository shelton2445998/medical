package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.ReportItemDetailDto;
import com.fourth.medical.medical.query.ReportItemDetailQuery;
import com.fourth.medical.medical.service.ReportItemDetailService;
import com.fourth.medical.medical.vo.ReportItemDetailVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 体检报告检查项明细 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>管理体检报告检查项的详细结果信息，包括具体的检查数值、异常标记、医生建议等</li>
 *   <li>支持检查项明细的增删改查操作，为体检报告提供详细的检查结果数据</li>
 *   <li>提供检查项明细的分页查询和详情查看功能</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>检查项明细管理：支持检查项详细结果信息的维护</li>
 *   <li>异常标记：支持检查结果的异常状态标记</li>
 *   <li>医生建议：支持医生对检查结果的建议和说明</li>
 *   <li>权限控制：基于角色的访问控制，确保数据安全</li>
 *   <li>操作日志：记录所有关键操作，便于审计和追踪</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>检查结果录入：医生或技师录入具体的检查结果数据</li>
 *   <li>异常判断：系统根据参考值自动判断检查结果是否异常</li>
 *   <li>医生审核：医生对检查结果进行审核并添加建议</li>
 *   <li>结果查询：支持按条件查询和分页展示检查项明细</li>
 *   <li>结果维护：根据需要进行检查结果的修正和更新</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>report:item:detail:add - 添加检查项明细权限</li>
 *   <li>report:item:detail:update - 修改检查项明细权限</li>
 *   <li>report:item:detail:delete - 删除检查项明细权限</li>
 *   <li>report:item:detail:info - 查看检查项明细详情权限</li>
 *   <li>report:item:detail:page - 查看检查项明细列表权限</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>输入验证：使用@Valid注解进行参数验证</li>
 *   <li>SQL注入防护：使用MyBatis参数化查询</li>
 *   <li>权限校验：每个接口都有对应的权限控制</li>
 *   <li>操作日志：记录所有关键操作，便于安全审计</li>
 *   <li>数据完整性：确保检查结果数据的准确性和完整性</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>结果模板：可扩展支持检查结果模板功能</li>
 *   <li>批量录入：可扩展支持批量录入检查结果</li>
 *   <li>结果对比：可扩展支持历史结果对比功能</li>
 *   <li>智能分析：可扩展支持检查结果的智能分析和预警</li>
 * </ul>
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "体检报告检查项明细")
@RequestMapping("/admin/reportItemDetail")
public class ReportItemDetailController {

    @Autowired
    private ReportItemDetailService reportItemDetailService;

    /**
     * 添加体检报告检查项明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加体检报告检查项明细")
    @PostMapping("/addReportItemDetail")
    @Permission("report:item:detail:add")
    public ApiResult addReportItemDetail(@Valid @RequestBody ReportItemDetailDto dto) {
        log.info("添加体检报告检查项明细：{}", dto);
        boolean flag = reportItemDetailService.addReportItemDetail(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改体检报告检查项明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改体检报告检查项明细")
    @PostMapping("/updateReportItemDetail")
    @Permission("report:item:detail:update")
    public ApiResult updateReportItemDetail(@Valid @RequestBody ReportItemDetailDto dto) {
        log.info("修改体检报告检查项明细：{}", dto);
        boolean flag = reportItemDetailService.updateReportItemDetail(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除体检报告检查项明细
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除体检报告检查项明细")
    @PostMapping("/deleteReportItemDetail/{id}")
    @Permission("report:item:detail:delete")
    public ApiResult deleteReportItemDetail(@PathVariable Long id) {
        log.info("删除体检报告检查项明细：{}", id);
        boolean flag = reportItemDetailService.deleteReportItemDetail(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取体检报告检查项明细详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检报告检查项明细详情")
    @PostMapping("/getReportItemDetail/{id}")
    @Permission("report:item:detail:info")
    public ApiResult<ReportItemDetailVo> getReportItemDetail(@PathVariable Long id) {
        log.info("获取体检报告检查项明细详情：{}", id);
        ReportItemDetailVo reportItemDetailVo = reportItemDetailService.getReportItemDetailById(id);
        return ApiResult.success(reportItemDetailVo);
    }

    /**
     * 获取体检报告检查项明细分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检报告检查项明细分页列表")
    @PostMapping("/getReportItemDetailPage")
    @Permission("report:item:detail:page")
    public ApiResult<ReportItemDetailVo> getReportItemDetailPage(@Valid @RequestBody ReportItemDetailQuery query) {
        log.info("获取体检报告检查项明细分页列表：{}", query);
        Paging<ReportItemDetailVo> paging = reportItemDetailService.getReportItemDetailPage(query);
        return ApiResult.success(paging);
    }

}
