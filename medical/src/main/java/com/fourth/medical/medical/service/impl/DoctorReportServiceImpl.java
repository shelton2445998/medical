package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
            // 根据医生ID查询待处理的报告数量
            // 待处理状态可能是 0 或其他表示未完成的状态值
            LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Report::getDoctorId, doctorId)
                   .eq(Report::getStatus, 0); // 假设0表示待处理状态
            
            Integer count = reportMapper.selectCount(wrapper);
            return count != null ? count : 0;
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
            
            Integer count = reportMapper.selectCount(wrapper);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("获取医生本月体检报告数量出错", e);
            return 0;
        }
    }
} 