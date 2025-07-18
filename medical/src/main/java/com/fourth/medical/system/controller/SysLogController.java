package com.fourth.medical.system.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.system.query.SysLogQuery;
import com.fourth.medical.system.service.SysLogService;
import com.fourth.medical.system.vo.SysLogVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 系统日志 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>管理系统操作日志，包括用户操作记录、系统事件日志、安全审计日志等</li>
 *   <li>支持系统日志的查询和查看功能，为系统监控和审计提供数据支撑</li>
 *   <li>提供日志的分页查询和详情查看功能</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>日志查询：支持系统日志的详情和列表查询</li>
 *   <li>日志分类：支持不同类型的日志分类管理</li>
 *   <li>日志分析：提供日志数据的统计分析功能</li>
 *   <li>权限控制：基于角色的访问控制，确保数据安全</li>
 *   <li>操作审计：记录所有关键操作，便于安全审计</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>日志记录：系统自动记录用户操作和系统事件</li>
 *   <li>日志查询：管理员查询和查看系统日志</li>
 *   <li>日志分析：对日志数据进行分析和统计</li>
 *   <li>安全审计：基于日志数据进行安全审计</li>
 *   <li>日志清理：定期清理过期的日志数据</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>sys:log:info - 查看日志详情权限</li>
 *   <li>sys:log:page - 查看日志列表权限</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>输入验证：使用@Valid注解进行参数验证</li>
 *   <li>SQL注入防护：使用MyBatis参数化查询</li>
 *   <li>权限校验：每个接口都有对应的权限控制</li>
 *   <li>数据脱敏：对敏感信息进行适当脱敏处理</li>
 *   <li>访问控制：确保只有授权用户才能访问日志数据</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>日志导出：可扩展支持日志数据导出功能</li>
 *   <li>实时监控：可扩展支持日志实时监控功能</li>
 *   <li>告警机制：可扩展支持基于日志的告警机制</li>
 *   <li>日志分析：可扩展支持更复杂的日志分析功能</li>
 * </ul>
 *
 * @author geekidea
 * @since 2023-02-16
 */
@Slf4j
@RestController
@Tag(name = "系统日志")
@RequestMapping("/admin/sysLog")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 获取系统日志详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysLog/{id}")
    @Operation(summary = "系统日志详情")
    @Permission("sys:log:info")
    public ApiResult<SysLogVo> getSysLog(@PathVariable Long id) {
        SysLogVo sysLogVo = sysLogService.getSysLogById(id);
        return ApiResult.success(sysLogVo);
    }

    /**
     * 系统日志分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @PostMapping("/getSysLogPage")
    @Operation(summary = "系统日志分页列表")
    @Permission("sys:log:page")
    public ApiResult<SysLogVo> getSysLogPage(@Valid @RequestBody SysLogQuery query) {
        Paging<SysLogVo> paging = sysLogService.getSysLogPage(query);
        return ApiResult.success(paging);
    }

}
