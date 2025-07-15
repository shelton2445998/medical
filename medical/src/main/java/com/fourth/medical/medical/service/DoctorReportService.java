package com.fourth.medical.medical.service;

import com.fourth.medical.framework.response.ApiResult;

/**
 * 医生报告服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface DoctorReportService {

    /**
     * 统计医生待处理的报告数量
     * 待处理报告定义为conclusion字段为空的报告
     *
     * @param doctorId 医生ID
     * @return 待处理报告数量
     */
    Integer countPendingReportsByDoctorId(Long doctorId);

    /**
     * 统计医生本月的体检报告数量
     * 统计该医生名下的所有报告数量，无论状态如何
     *
     * @param doctorId 医生ID
     * @return 本月体检报告数量
     */
    Integer countMonthReportsByDoctorId(Long doctorId);
    
    /**
     * 获取医生待处理报告列表
     * 待处理报告定义为conclusion字段为空的报告
     *
     * @param doctorId 医生ID
     * @return 待处理报告列表
     */
    ApiResult getPendingReportsByDoctorId(Long doctorId);
} 