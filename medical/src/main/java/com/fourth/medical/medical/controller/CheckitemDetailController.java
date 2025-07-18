package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.CheckitemDetailDto;
import com.fourth.medical.medical.query.CheckitemDetailQuery;
import com.fourth.medical.medical.service.CheckitemDetailService;
import com.fourth.medical.medical.vo.CheckitemDetailVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 检查项明细 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>管理体检检查项的详细配置信息，包括检查项的具体参数、检查方法、注意事项等</li>
 *   <li>支持检查项明细的增删改查操作，为体检套餐配置提供详细数据支撑</li>
 *   <li>提供检查项明细的分页查询和详情查看功能</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>检查项明细管理：支持检查项详细配置信息的维护</li>
 *   <li>参数配置：支持检查项的具体参数和检查方法配置</li>
 *   <li>注意事项：支持检查项的注意事项和准备要求管理</li>
 *   <li>权限控制：基于角色的访问控制，确保数据安全</li>
 *   <li>操作日志：记录所有关键操作，便于审计和追踪</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>明细创建：管理员创建检查项明细，配置具体参数和检查方法</li>
 *   <li>明细维护：根据业务需求更新检查项明细信息</li>
 *   <li>明细查询：支持按条件查询和分页展示检查项明细列表</li>
 *   <li>明细删除：删除不再使用的检查项明细（需谨慎操作）</li>
 *   <li>套餐关联：将检查项明细关联到具体的体检套餐中</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>checkitem:detail:add - 添加检查项明细权限</li>
 *   <li>checkitem:detail:update - 修改检查项明细权限</li>
 *   <li>checkitem:detail:delete - 删除检查项明细权限</li>
 *   <li>checkitem:detail:info - 查看检查项明细详情权限</li>
 *   <li>checkitem:detail:page - 查看检查项明细列表权限</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>输入验证：使用@Valid注解进行参数验证</li>
 *   <li>SQL注入防护：使用MyBatis参数化查询</li>
 *   <li>权限校验：每个接口都有对应的权限控制</li>
 *   <li>操作日志：记录所有关键操作，便于安全审计</li>
 *   <li>数据完整性：确保检查项明细数据的准确性和完整性</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>模板管理：可扩展支持检查项明细模板功能</li>
 *   <li>批量操作：可扩展支持批量导入、导出检查项明细</li>
 *   <li>版本控制：可扩展支持检查项明细版本管理</li>
 *   <li>智能推荐：可扩展支持基于用户特征的检查项推荐</li>
 * </ul>
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "检查项明细")
@RequestMapping("/admin/checkitemDetail")
public class CheckitemDetailController {

    @Autowired
    private CheckitemDetailService checkitemDetailService;

    /**
     * 添加检查项明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加检查项明细")
    @PostMapping("/addCheckitemDetail")
    @Permission("checkitem:detail:add")
    public ApiResult addCheckitemDetail(@Valid @RequestBody CheckitemDetailDto dto) {
        log.info("添加检查项明细：{}", dto);
        boolean flag = checkitemDetailService.addCheckitemDetail(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改检查项明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改检查项明细")
    @PostMapping("/updateCheckitemDetail")
    @Permission("checkitem:detail:update")
    public ApiResult updateCheckitemDetail(@Valid @RequestBody CheckitemDetailDto dto) {
        log.info("修改检查项明细：{}", dto);
        boolean flag = checkitemDetailService.updateCheckitemDetail(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除检查项明细
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除检查项明细")
    @PostMapping("/deleteCheckitemDetail/{id}")
    @Permission("checkitem:detail:delete")
    public ApiResult deleteCheckitemDetail(@PathVariable Long id) {
        log.info("删除检查项明细：{}", id);
        boolean flag = checkitemDetailService.deleteCheckitemDetail(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取检查项明细详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取检查项明细详情")
    @PostMapping("/getCheckitemDetail/{id}")
    @Permission("checkitem:detail:info")
    public ApiResult<CheckitemDetailVo> getCheckitemDetail(@PathVariable Long id) {
        log.info("获取检查项明细详情：{}", id);
        CheckitemDetailVo checkitemDetailVo = checkitemDetailService.getCheckitemDetailById(id);
        return ApiResult.success(checkitemDetailVo);
    }

    /**
     * 获取检查项明细分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取检查项明细分页列表")
    @PostMapping("/getCheckitemDetailPage")
    @Permission("checkitem:detail:page")
    public ApiResult<CheckitemDetailVo> getCheckitemDetailPage(@Valid @RequestBody CheckitemDetailQuery query) {
        log.info("获取检查项明细分页列表：{}", query);
        Paging<CheckitemDetailVo> paging = checkitemDetailService.getCheckitemDetailPage(query);
        return ApiResult.success(paging);
    }

}
