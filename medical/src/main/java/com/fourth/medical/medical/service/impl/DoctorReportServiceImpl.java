package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.entity.Report;
import com.fourth.medical.medical.mapper.ReportMapper;
import com.fourth.medical.medical.service.DoctorReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医生报告服务实现类
 *
 * @author fourth
 * @since 2023-07-09
 */
@Slf4j
@Service
public class DoctorReportServiceImpl implements DoctorReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public Integer countPendingReportsByDoctorId(Long doctorId) {
        log.info("获取医生[{}]待处理报告数量", doctorId);
        try {
            // 修改查询条件：待处理报告定义为conclusion为空的报告
            LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Report::getDoctorId, doctorId)
                   .isNull(Report::getConclusion)
                   .or()
                   .eq(Report::getConclusion, "");
            
            Long count = reportMapper.selectCount(wrapper);
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            log.error("获取医生待处理报告数量出错", e);
            return 0;
        }
    }

    @Override
    public Integer countMonthReportsByDoctorId(Long doctorId) {
        log.info("获取医生[{}]本月体检报告数量", doctorId);
        try {
            // 获取本月第一天和最后一天
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date firstDay = calendar.getTime();
            
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.SECOND, -1);
            Date lastDay = calendar.getTime();
            
            // 根据医生ID和时间范围查询报告数量
            LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Report::getDoctorId, doctorId)
                   .ge(Report::getCreateTime, firstDay)
                   .le(Report::getCreateTime, lastDay);
            
            Long count = reportMapper.selectCount(wrapper);
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            log.error("获取医生本月体检报告数量出错", e);
            return 0;
        }
    }
    
    @Override
    public ApiResult getPendingReportsByDoctorId(Long doctorId) {
        log.info("获取医生[{}]待处理报告列表", doctorId);
        try {
            // 查询条件：待处理报告定义为conclusion为空的报告
            LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Report::getDoctorId, doctorId)
                   .isNull(Report::getConclusion)
                   .or()
                   .eq(Report::getConclusion, "")
                   .orderByDesc(Report::getCreateTime); // 按创建时间降序排序
            
            // 分页查询
            Page<Report> page = new Page<>(1, 50); // 默认查询第一页，每页50条
            Page<Report> reportPage = reportMapper.selectPage(page, wrapper);
            
            // 封装返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("records", reportPage.getRecords());
            result.put("total", reportPage.getTotal());
            result.put("pages", reportPage.getPages());
            result.put("current", reportPage.getCurrent());
            result.put("size", reportPage.getSize());
            
            return ApiResult.success(result);
        } catch (Exception e) {
            log.error("获取医生待处理报告列表出错", e);
            return ApiResult.fail("获取待处理报告列表失败：" + e.getMessage());
        }
    }
} 