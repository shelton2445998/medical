package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.ReportItem;
import com.fourth.medical.medical.query.ReportItemQuery;
import com.fourth.medical.medical.vo.ReportItemVo;
import com.fourth.medical.medical.query.AppReportItemQuery;
import com.fourth.medical.medical.vo.AppReportItemVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 体检报告检查项信息 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-09
 */
@Mapper
public interface ReportItemMapper extends BaseMapper<ReportItem> {

    /**
     * 体检报告检查项信息详情
     *
     * @param id
     * @return
     */
    ReportItemVo getReportItemById(Long id);

    /**
     * 体检报告检查项信息分页列表
     *
     * @param query
     * @return
     */
    List<ReportItemVo> getReportItemPage(ReportItemQuery query);

    /**
     * App体检报告检查项信息详情
     *
     * @param id
     * @return
     */
    AppReportItemVo getAppReportItemById(Long id);

    /**
     * App体检报告检查项信息分页列表
     *
     * @param query
     * @return
     */
    List<AppReportItemVo> getAppReportItemPage(AppReportItemQuery query);

}
