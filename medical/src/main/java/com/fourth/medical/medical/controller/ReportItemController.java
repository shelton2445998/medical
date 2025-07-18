package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.ReportItemDto;
import com.fourth.medical.medical.query.ReportItemQuery;
import com.fourth.medical.medical.service.ReportItemService;
import com.fourth.medical.medical.vo.ReportItemVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 体检报告检查项信息 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>管理体检报告中的检查项信息，包括检查项的基本信息、检查结果、参考值等</li>
 *   <li>支持检查项的增删改查操作，为体检报告生成提供数据支撑</li>
 *   <li>提供检查项的分页查询和详情查看功能</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>检查项信息管理：支持检查项的基本信息维护</li>
 *   <li>权限控制：基于角色的访问控制，确保数据安全</li>
 *   <li>操作日志：记录所有关键操作，便于审计和追踪</li>
 *   <li>数据验证：对输入数据进行严格验证，确保数据完整性</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>检查项创建：管理员创建新的检查项，设置检查项名称、类型、参考值等</li>
 *   <li>检查项维护：根据业务需求更新检查项信息</li>
 *   <li>检查项查询：支持按条件查询和分页展示检查项列表</li>
 *   <li>检查项删除：删除不再使用的检查项（需谨慎操作）</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>report:item:add - 添加检查项权限</li>
 *   <li>report:item:update - 修改检查项权限</li>
 *   <li>report:item:delete - 删除检查项权限</li>
 *   <li>report:item:info - 查看检查项详情权限</li>
 *   <li>report:item:page - 查看检查项列表权限</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>输入验证：使用@Valid注解进行参数验证</li>
 *   <li>SQL注入防护：使用MyBatis参数化查询</li>
 *   <li>权限校验：每个接口都有对应的权限控制</li>
 *   <li>操作日志：记录所有关键操作，便于安全审计</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>检查项模板：可扩展支持检查项模板功能</li>
 *   <li>批量操作：可扩展支持批量导入、导出检查项</li>
 *   <li>检查项分类：可扩展支持检查项分类管理</li>
 *   <li>版本控制：可扩展支持检查项版本管理</li>
 * </ul>
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "体检报告检查项信息")
@RequestMapping("/admin/reportItem")
public class ReportItemController {

    @Autowired
    private ReportItemService reportItemService;

    /**
     * 添加体检报告检查项信息
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加体检报告检查项信息")
    @PostMapping("/addReportItem")
    @Permission("report:item:add")
    public ApiResult addReportItem(@Valid @RequestBody ReportItemDto dto) {
        log.info("添加体检报告检查项信息：{}", dto);
        boolean flag = reportItemService.addReportItem(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改体检报告检查项信息
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改体检报告检查项信息")
    @PostMapping("/updateReportItem")
    @Permission("report:item:update")
    public ApiResult updateReportItem(@Valid @RequestBody ReportItemDto dto) {
        log.info("修改体检报告检查项信息：{}", dto);
        boolean flag = reportItemService.updateReportItem(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除体检报告检查项信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除体检报告检查项信息")
    @PostMapping("/deleteReportItem/{id}")
    @Permission("report:item:delete")
    public ApiResult deleteReportItem(@PathVariable Long id) {
        log.info("删除体检报告检查项信息：{}", id);
        boolean flag = reportItemService.deleteReportItem(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取体检报告检查项信息详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检报告检查项信息详情")
    @PostMapping("/getReportItem/{id}")
    @Permission("report:item:info")
    public ApiResult<ReportItemVo> getReportItem(@PathVariable Long id) {
        log.info("获取体检报告检查项信息详情：{}", id);
        ReportItemVo reportItemVo = reportItemService.getReportItemById(id);
        return ApiResult.success(reportItemVo);
    }

    /**
     * 获取体检报告检查项信息分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检报告检查项信息分页列表")
    @PostMapping("/getReportItemPage")
    @Permission("report:item:page")
    public ApiResult<ReportItemVo> getReportItemPage(@Valid @RequestBody ReportItemQuery query) {
        log.info("获取体检报告检查项信息分页列表：{}", query);
        Paging<ReportItemVo> paging = reportItemService.getReportItemPage(query);
        return ApiResult.success(paging);
    }

}
