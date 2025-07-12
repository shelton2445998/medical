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
 * @author fourth
 * @since 2025-07-12
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
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param request   HTTP请求对象，用于获取请求头中的token
     * @return 医生排班列表
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
     * @param dto     排班信息
     * @param request HTTP请求对象，用于获取请求头中的token
     * @return 添加结果
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
     * 删除医生排班
     *
     * @param id      排班ID
     * @param request HTTP请求对象，用于获取请求头中的token
     * @return 删除结果
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