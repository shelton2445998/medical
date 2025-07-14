package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.Report;
import com.fourth.medical.medical.query.ReportQuery;
import com.fourth.medical.medical.vo.ReportVo;
import com.fourth.medical.medical.query.AppReportQuery;
import com.fourth.medical.medical.vo.AppReportVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 体检报告总 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-14
 */
@Mapper
public interface ReportMapper extends BaseMapper<Report> {

    /**
     * 体检报告总详情
     *
     * @param id
     * @return
     */
    ReportVo getReportById(Long id);

    /**
     * 体检报告总分页列表
     *
     * @param query
     * @return
     */
    List<ReportVo> getReportPage(ReportQuery query);

    /**
     * App体检报告总详情
     *
     * @param id
     * @return
     */
    AppReportVo getAppReportById(Long id);

    /**
     * App体检报告总分页列表
     *
     * @param query
     * @return
     */
    List<AppReportVo> getAppReportPage(AppReportQuery query);

}
