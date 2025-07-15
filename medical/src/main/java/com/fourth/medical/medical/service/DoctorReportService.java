package com.fourth.medical.medical.service;

/**
 * 医生报告服务接口
 *
 * @author fourth
 * @since 2023-07-09
 */
public interface DoctorReportService {

    /**
     * 获取医生待处理报告数量
     *
     * @param doctorId 医生ID
     * @return 待处理报告数量
     */
    Integer countPendingReportsByDoctorId(Long doctorId);

    /**
     * 获取医生本月体检报告数量
     *
     * @param doctorId 医生ID
     * @return 本月体检报告数量
     */
    Integer countMonthReportsByDoctorId(Long doctorId);
} 