package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppDoctorScheduleQuery;
import com.fourth.medical.medical.service.DoctorScheduleService;
import com.fourth.medical.medical.vo.AppDoctorScheduleVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App医生排班 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>为移动端APP提供医生排班查询服务，包括医生排班详情、排班列表、可预约时间等</li>
 *   <li>支持医生排班的详情查看和分页查询功能</li>
 *   <li>为移动端用户提供便捷的医生预约服务</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>移动端适配：专门为移动端APP设计的接口</li>
 *   <li>排班查询：支持医生排班的详情和列表查询</li>
 *   <li>可预约时间：显示医生的可预约时间段</li>
 *   <li>用户友好：提供简洁易用的移动端数据格式</li>
 *   <li>实时更新：排班信息实时更新，确保数据准确性</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>医生选择：用户选择要预约的医生</li>
 *   <li>排班查看：用户查看医生的排班信息</li>
 *   <li>时间选择：用户选择可预约的时间段</li>
 *   <li>预约确认：用户确认预约信息</li>
 *   <li>数据同步：与后端排班数据保持同步</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>移动端用户认证：通过用户登录状态进行身份验证</li>
 *   <li>数据权限：所有用户都可以查看医生排班信息</li>
 *   <li>接口安全：通过Token验证确保接口调用安全</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>数据传输：使用HTTPS确保数据传输安全</li>
 *   <li>Token验证：通过JWT Token进行身份验证</li>
 *   <li>数据缓存：合理使用缓存提高查询性能</li>
 *   <li>实时性：确保排班信息的实时性和准确性</li>
 *   <li>隐私保护：保护医生个人信息的隐私安全</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>智能推荐：可扩展支持基于用户偏好的医生推荐</li>
 *   <li>预约提醒：可扩展支持预约时间提醒功能</li>
 *   <li>医生评价：可扩展支持医生评价和评分功能</li>
 *   <li>在线咨询：可扩展支持在线咨询功能</li>
 * </ul>
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App医生排班")
@RequestMapping("/app/doctorSchedule")
public class AppDoctorScheduleController {

    @Autowired
    private DoctorScheduleService doctorScheduleService;

    /**
     * 获取App医生排班详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App医生排班详情")
    @PostMapping("/getAppDoctorSchedule/{id}")
    public ApiResult<AppDoctorScheduleVo> getAppDoctorSchedule(@PathVariable Long id) {
        log.info("获取App医生排班详情：{}", id);
        AppDoctorScheduleVo appDoctorScheduleVo = doctorScheduleService.getAppDoctorScheduleById(id);
        return ApiResult.success(appDoctorScheduleVo);
    }

    /**
     * 获取App医生排班分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App医生排班分页列表")
    @PostMapping("/getAppDoctorSchedulePage")
    public ApiResult<AppDoctorScheduleVo> getAppDoctorSchedulePage(@Valid @RequestBody AppDoctorScheduleQuery query) {
        log.info("获取App医生排班分页列表：{}", query);
        Paging<AppDoctorScheduleVo> paging = doctorScheduleService.getAppDoctorSchedulePage(query);
        return ApiResult.success(paging);
    }

}
