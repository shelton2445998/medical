package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.util.LoginUtil;
import com.fourth.medical.auth.util.TokenUtil;
import com.fourth.medical.auth.vo.LoginVo;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.DoctorScheduleDto;
import com.fourth.medical.medical.query.DoctorScheduleQuery;
import com.fourth.medical.medical.service.DoctorScheduleService;
import com.fourth.medical.medical.vo.DoctorScheduleVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * 医生端排班查询控制器
 * 
 * 功能说明：
 * 本控制器为医生端提供排班管理服务，支持医生查看自己的排班信息、添加排班、修改排班、
 * 删除排班等功能。医生可以通过此控制器管理自己的工作时间安排，确保工作安排的合理性。
 * 
 * 主要功能：
 * 1. 获取医生排班列表 - 查询医生在指定时间范围内的排班信息
 * 2. 添加医生排班 - 医生为自己添加新的排班安排
 * 3. 更新医生排班 - 修改已存在的排班信息
 * 4. 删除医生排班 - 删除指定的排班安排
 * 
 * 业务流程：
 * 1. 医生登录系统，获取认证Token
 * 2. 医生查看自己的排班信息
 * 3. 医生根据需要添加、修改或删除排班
 * 4. 系统验证排班安排的合理性
 * 5. 保存排班信息到数据库
 * 6. 返回操作结果给医生
 * 
 * 权限控制：
 * - 医生只能查看和操作自己的排班信息
 * - 基于医生的Token进行身份验证
 * - 排班操作需要有效的医生身份
 * 
 * 数据安全：
 * - 严格的Token验证机制
 * - 医生身份验证和授权
 * - 排班数据的访问控制
 * - 操作日志记录和审计
 * 
 * 业务规则：
 * - 不能添加过去的排班
 * - 排班时间不能冲突
 * - 排班容量不能超过限制
 * - 已预约的排班不能删除
 * 
 * 扩展功能：
 * - 支持多种查询条件组合
 * - 支持排班数据的导出
 * - 支持移动端访问
 * - 支持消息推送通知
 * 
 * @author fourth
 * @since 2025-07-12
 * @version 1.0
 */
@Slf4j
@RestController
@Tag(name = "医生排班查询")
@RequestMapping("/doctor/schedule")
public class DoctorScheduleController {

    @Autowired
    private DoctorScheduleService doctorScheduleService;

    /**
     * 获取医生排班列表
     * 
     * 功能说明：
     * 查询当前登录医生在指定时间范围内的排班信息，支持按开始日期和结束日期进行筛选。
     * 医生只能查看自己的排班信息。
     * 
     * 业务流程：
     * 1. 从请求头中获取医生的Token
     * 2. 验证Token的有效性和医生身份
     * 3. 获取医生的ID信息
     * 4. 构建查询条件，包括时间范围
     * 5. 调用服务层查询排班列表
     * 6. 返回排班信息列表
     * 
     * 权限控制：
     * - 医生只能查看自己的排班
     * - 严格的Token验证机制
     * 
     * 查询条件：
     * - startDate：开始日期，可选参数
     * - endDate：结束日期，可选参数
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param request HTTP请求对象，用于获取请求头中的token
     * @return ApiResult<List<DoctorScheduleVo>> 医生排班列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取医生排班列表")
    public ApiResult<List<DoctorScheduleVo>> getDoctorScheduleList(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            HttpServletRequest request) {
        
        // 从请求头中获取token
        String token = TokenUtil.getToken(request);
        if (StringUtils.isBlank(token)) {
            return ApiResult.fail("未登录或登录已过期");
        }
        
        // 通过token获取医生信息
        LoginVo loginVo = LoginUtil.getLoginVo(token);
        if (loginVo == null) {
            return ApiResult.fail("无法获取医生信息，请重新登录");
        }
        
        Long doctorId = loginVo.getUserId();
        log.info("获取医生排班列表: doctorId={}, startDate={}, endDate={}, token={}", doctorId, startDate, endDate, token);
        
        // 构建查询条件
        DoctorScheduleQuery query = new DoctorScheduleQuery();
        query.setStartDate(startDate);
        query.setEndDate(endDate);
        
        // 获取排班列表
        List<DoctorScheduleVo> scheduleList = doctorScheduleService.getDoctorScheduleListByDoctorId(doctorId, query);
        return ApiResult.success(scheduleList);
    }
    
    /**
     * 添加医生排班
     * 
     * 功能说明：
     * 医生为自己添加新的排班安排，包括工作日期、时间段、容量等信息。
     * 系统会验证排班安排的合理性。
     * 
     * 业务流程：
     * 1. 从请求头中获取医生的Token
     * 2. 验证Token的有效性和医生身份
     * 3. 获取医生的ID信息
     * 4. 验证排班数据的有效性
     * 5. 检查排班时间是否合理
     * 6. 保存排班信息到数据库
     * 7. 返回操作结果
     * 
     * 数据验证：
     * - 工作日期不能为过去的日期
     * - 排班时间不能冲突
     * - 容量设置必须合理
     * 
     * 权限控制：
     * - 医生只能为自己添加排班
     * - 严格的Token验证机制
     * 
     * @param dto 排班信息数据传输对象
     * @param request HTTP请求对象，用于获取请求头中的token
     * @return ApiResult 添加结果
     */
    @PostMapping("/add")
    @Operation(summary = "添加医生排班")
    public ApiResult addDoctorSchedule(@RequestBody DoctorScheduleDto dto, HttpServletRequest request) {
        // 从请求头中获取token
        String token = TokenUtil.getToken(request);
        if (StringUtils.isBlank(token)) {
            return ApiResult.fail("未登录或登录已过期");
        }
        
        // 通过token获取医生信息
        LoginVo loginVo = LoginUtil.getLoginVo(token);
        if (loginVo == null) {
            return ApiResult.fail("无法获取医生信息，请重新登录");
        }
        
        Long doctorId = loginVo.getUserId();
        log.info("添加医生排班: doctorId={}, dto={}", doctorId, dto);
        
        // 设置医生ID
        dto.setDoctorId(doctorId);
        
        // 初始化已预约人数为0
        dto.setReservedNumber(0);
        
        // 校验工作日期不能为过去的日期
        Date currentDate = new Date();
        if (dto.getWorkDate().before(currentDate)) {
            return ApiResult.fail("不能添加过去的排班");
        }
        
        // 在设置了doctorId后再进行验证
        try {
            // 添加医生排班
            boolean flag = doctorScheduleService.addDoctorSchedule(dto);
            return ApiResult.result(flag);
        } catch (Exception e) {
            log.error("添加医生排班失败", e);
            return ApiResult.fail("添加排班失败：" + e.getMessage());
        }
    }

    /**
     * 更新医生排班
     * 
     * 功能说明：
     * 医生修改已存在的排班信息，包括工作日期、时间段、容量等。
     * 系统会验证修改的合理性。
     * 
     * 业务流程：
     * 1. 从请求头中获取医生的Token
     * 2. 验证Token的有效性和医生身份
     * 3. 获取医生的ID信息
     * 4. 验证排班是否存在
     * 5. 检查是否是本人的排班
     * 6. 验证修改数据的有效性
     * 7. 更新排班信息
     * 8. 返回操作结果
     * 
     * 数据验证：
     * - 排班必须存在
     * - 只能修改自己的排班
     * - 修改后的时间不能冲突
     * 
     * 权限控制：
     * - 医生只能修改自己的排班
     * - 严格的Token验证机制
     * 
     * @param dto 排班信息数据传输对象
     * @param request HTTP请求对象，用于获取请求头中的token
     * @return ApiResult 更新结果
     */
    @PostMapping("/update")
    @Operation(summary = "更新医生排班")
    public ApiResult updateDoctorSchedule(@RequestBody DoctorScheduleDto dto, HttpServletRequest request) {
        // 从请求头中获取token
        String token = TokenUtil.getToken(request);
        if (StringUtils.isBlank(token)) {
            return ApiResult.fail("未登录或登录已过期");
        }
        
        // 通过token获取医生信息
        LoginVo loginVo = LoginUtil.getLoginVo(token);
        if (loginVo == null) {
            return ApiResult.fail("无法获取医生信息，请重新登录");
        }
        
        Long doctorId = loginVo.getUserId();
        log.info("更新医生排班: doctorId={}, dto={}", doctorId, dto);
        
        // 获取排班信息
        DoctorScheduleVo scheduleVo = doctorScheduleService.getDoctorScheduleById(dto.getId());
        if (scheduleVo == null) {
            return ApiResult.fail("排班不存在");
        }
        
        // 检查是否是本人的排班
        if (!doctorId.equals(scheduleVo.getDoctorId())) {
            return ApiResult.fail("只能修改自己的排班");
        }
        
        try {
            // 更新医生排班
            boolean flag = doctorScheduleService.updateDoctorSchedule(dto);
            return ApiResult.result(flag);
        } catch (Exception e) {
            log.error("更新医生排班失败", e);
            return ApiResult.fail("更新排班失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除医生排班
     * 
     * 功能说明：
     * 医生删除指定的排班安排。系统会检查排班是否可以删除。
     * 
     * 业务流程：
     * 1. 从请求头中获取医生的Token
     * 2. 验证Token的有效性和医生身份
     * 3. 获取医生的ID信息
     * 4. 验证排班是否存在
     * 5. 检查是否是本人的排班
     * 6. 检查排班是否可以删除
     * 7. 删除排班信息
     * 8. 返回操作结果
     * 
     * 数据验证：
     * - 排班必须存在
     * - 只能删除自己的排班
     * - 已预约的排班不能删除
     * 
     * 权限控制：
     * - 医生只能删除自己的排班
     * - 严格的Token验证机制
     * 
     * @param id 排班ID
     * @param request HTTP请求对象，用于获取请求头中的token
     * @return ApiResult 删除结果
     */
    @PostMapping("/delete/{id}")
    @Operation(summary = "删除医生排班")
    public ApiResult deleteDoctorSchedule(@PathVariable Long id, HttpServletRequest request) {
        // 从请求头中获取token
        String token = TokenUtil.getToken(request);
        if (StringUtils.isBlank(token)) {
            return ApiResult.fail("未登录或登录已过期");
        }
        
        // 通过token获取医生信息
        LoginVo loginVo = LoginUtil.getLoginVo(token);
        if (loginVo == null) {
            return ApiResult.fail("无法获取医生信息，请重新登录");
        }
        
        Long doctorId = loginVo.getUserId();
        log.info("删除医生排班: doctorId={}, scheduleId={}", doctorId, id);
        
        // 获取排班信息
        DoctorScheduleVo scheduleVo = doctorScheduleService.getDoctorScheduleById(id);
        if (scheduleVo == null) {
            return ApiResult.fail("排班不存在");
        }
        
        // 检查是否是本人的排班
        if (!doctorId.equals(scheduleVo.getDoctorId())) {
            return ApiResult.fail("只能删除自己的排班");
        }
        
        // 检查是否已有预约
        if (scheduleVo.getReservedNumber() > 0) {
            return ApiResult.fail("已有患者预约，不能删除");
        }
        
        // 检查是否是7天以内的排班
        Date workDate = scheduleVo.getWorkDate();
        LocalDate scheduleDate = workDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        LocalDate sevenDaysLater = currentDate.plusDays(7);
        
        if (scheduleDate.isBefore(sevenDaysLater)) {
            return ApiResult.fail("7天内的排班不能删除");
        }
        
        // 删除医生排班
        boolean flag = doctorScheduleService.deleteDoctorSchedule(id);
        return ApiResult.result(flag);
    }
} 