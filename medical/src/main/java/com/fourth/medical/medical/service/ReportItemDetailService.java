package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.ReportItemDetailDto;
import com.fourth.medical.medical.entity.ReportItemDetail;
import com.fourth.medical.medical.query.ReportItemDetailQuery;
import com.fourth.medical.medical.vo.ReportItemDetailVo;
import com.fourth.medical.medical.query.AppReportItemDetailQuery;
import com.fourth.medical.medical.vo.AppReportItemDetailVo;


/**
 * 体检报告检查项明细 服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface ReportItemDetailService extends IService<ReportItemDetail> {

    /**
     * 添加体检报告检查项明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addReportItemDetail(ReportItemDetailDto dto);

    /**
     * 修改体检报告检查项明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateReportItemDetail(ReportItemDetailDto dto);

    /**
     * 删除体检报告检查项明细
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteReportItemDetail(Long id);

    /**
     * 体检报告检查项明细详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    ReportItemDetailVo getReportItemDetailById(Long id);

    /**
     * 体检报告检查项明细分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<ReportItemDetailVo> getReportItemDetailPage(ReportItemDetailQuery query);

    /**
     * App体检报告检查项明细详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppReportItemDetailVo getAppReportItemDetailById(Long id);

    /**
     * App体检报告检查项明细分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppReportItemDetailVo> getAppReportItemDetailPage(AppReportItemDetailQuery query);

}
