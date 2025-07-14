package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.ReportDto;
import com.fourth.medical.medical.entity.Report;
import com.fourth.medical.medical.query.ReportQuery;
import com.fourth.medical.medical.query.AppReportQuery;
import com.fourth.medical.medical.vo.ReportVo;
import com.fourth.medical.medical.vo.AppReportVo;

/**
 * 体检报告总 服务接口
 *
 * @author fourth
 * @since 2025-07-14
 */
public interface ReportService extends IService<Report> {

    /**
     * 添加体检报告总
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addReport(ReportDto dto);

    /**
     * 修改体检报告总
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateReport(ReportDto dto);

    /**
     * 删除体检报告总
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteReport(Long id);

    /**
     * 体检报告总详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    ReportVo getReportById(Long id);

    /**
     * 体检报告总分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<ReportVo> getReportPage(ReportQuery query);

    /**
     * App体检报告总详情
     *
     * @param id
     * @param token 用户令牌
     * @return
     * @throws Exception
     */
    AppReportVo getAppReportById(Long id, String token);

    /**
     * App体检报告总分页列表
     *
     * @param query
     * @param token 用户令牌
     * @return
     * @throws Exception
     */
    Paging<AppReportVo> getAppReportPage(AppReportQuery query, String token);
    
    /**
     * 为订单创建体检报告
     *
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param checkitemIds 检查项ID列表，以逗号分隔
     * @param doctorId 医生ID
     * @return 报告ID
     */
    Long createReportForOrder(Long orderId, Long userId, String checkitemIds, Long doctorId);
}
