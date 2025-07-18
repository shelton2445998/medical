package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppDepartmentQuery;
import com.fourth.medical.medical.service.DepartmentService;
import com.fourth.medical.medical.vo.AppDepartmentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App科室 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>为移动端APP提供科室信息查询服务，包括科室详情、科室列表、科室介绍等</li>
 *   <li>支持科室信息的详情查看和分页查询功能</li>
 *   <li>为移动端用户提供便捷的科室选择服务</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>移动端适配：专门为移动端APP设计的接口</li>
 *   <li>科室信息查询：支持科室详情和列表查询</li>
 *   <li>科室介绍：提供科室的详细介绍和特色服务</li>
 *   <li>用户友好：提供简洁易用的移动端数据格式</li>
 *   <li>性能优化：针对移动端网络环境进行性能优化</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>科室浏览：用户浏览医院的科室列表</li>
 *   <li>科室详情：用户查看具体科室的详细信息</li>
 *   <li>科室选择：用户选择合适的科室进行体检</li>
 *   <li>医生查询：用户查看科室下的医生信息</li>
 *   <li>信息同步：与后端科室数据保持同步</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>公开访问：科室信息为公开数据，无需登录即可访问</li>
 *   <li>数据权限：所有用户都可以查看科室信息</li>
 *   <li>接口安全：通过HTTPS确保数据传输安全</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>数据传输：使用HTTPS确保数据传输安全</li>
 *   <li>数据缓存：合理使用缓存提高查询性能</li>
 *   <li>数据准确性：确保科室信息的准确性和时效性</li>
 *   <li>内容安全：对科室介绍内容进行安全过滤</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>科室导航：可扩展支持科室位置导航功能</li>
 *   <li>智能推荐：可扩展支持基于用户偏好的科室推荐</li>
 *   <li>科室评价：可扩展支持科室评价和评分功能</li>
 *   <li>在线咨询：可扩展支持科室相关的在线咨询</li>
 * </ul>
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App科室")
@RequestMapping("/app/department")
public class AppDepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 获取App科室详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App科室详情")
    @PostMapping("/getAppDepartment/{id}")
    public ApiResult<AppDepartmentVo> getAppDepartment(@PathVariable Long id) {
        log.info("获取App科室详情：{}", id);
        AppDepartmentVo appDepartmentVo = departmentService.getAppDepartmentById(id);
        return ApiResult.success(appDepartmentVo);
    }

    /**
     * 获取App科室分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App科室分页列表")
    @PostMapping("/getAppDepartmentPage")
    public ApiResult<AppDepartmentVo> getAppDepartmentPage(@Valid @RequestBody AppDepartmentQuery query) {
        log.info("获取App科室分页列表：{}", query);
        Paging<AppDepartmentVo> paging = departmentService.getAppDepartmentPage(query);
        return ApiResult.success(paging);
    }

}
