package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.ReportItemDto;
import com.fourth.medical.medical.entity.ReportItem;
import com.fourth.medical.medical.query.ReportItemQuery;
import com.fourth.medical.medical.vo.ReportItemVo;
import com.fourth.medical.medical.query.AppReportItemQuery;
import com.fourth.medical.medical.vo.AppReportItemVo;


/**
 * 体检报告检查项信息 服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface ReportItemService extends IService<ReportItem> {

    /**
     * 添加体检报告检查项信息
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addReportItem(ReportItemDto dto);

    /**
     * 修改体检报告检查项信息
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateReportItem(ReportItemDto dto);

    /**
     * 删除体检报告检查项信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteReportItem(Long id);

    /**
     * 体检报告检查项信息详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    ReportItemVo getReportItemById(Long id);

    /**
     * 体检报告检查项信息分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<ReportItemVo> getReportItemPage(ReportItemQuery query);

    /**
     * App体检报告检查项信息详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppReportItemVo getAppReportItemById(Long id);

    /**
     * App体检报告检查项信息分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppReportItemVo> getAppReportItemPage(AppReportItemQuery query);

}
