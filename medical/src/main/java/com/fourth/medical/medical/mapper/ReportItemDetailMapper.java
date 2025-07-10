package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.ReportItemDetail;
import com.fourth.medical.medical.query.ReportItemDetailQuery;
import com.fourth.medical.medical.vo.ReportItemDetailVo;
import com.fourth.medical.medical.query.AppReportItemDetailQuery;
import com.fourth.medical.medical.vo.AppReportItemDetailVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 体检报告检查项明细 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-09
 */
@Mapper
public interface ReportItemDetailMapper extends BaseMapper<ReportItemDetail> {

    /**
     * 体检报告检查项明细详情
     *
     * @param id
     * @return
     */
    ReportItemDetailVo getReportItemDetailById(Long id);

    /**
     * 体检报告检查项明细分页列表
     *
     * @param query
     * @return
     */
    List<ReportItemDetailVo> getReportItemDetailPage(ReportItemDetailQuery query);

    /**
     * App体检报告检查项明细详情
     *
     * @param id
     * @return
     */
    AppReportItemDetailVo getAppReportItemDetailById(Long id);

    /**
     * App体检报告检查项明细分页列表
     *
     * @param query
     * @return
     */
    List<AppReportItemDetailVo> getAppReportItemDetailPage(AppReportItemDetailQuery query);

}
