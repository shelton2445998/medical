package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.entity.ReportItem;
import com.fourth.medical.medical.query.ReportItemQuery;
import com.fourth.medical.medical.vo.ReportItemVo;
import com.fourth.medical.medical.query.AppReportItemQuery;
import com.fourth.medical.medical.vo.AppReportItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

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
    ReportItemVo getReportItemById(@Param("id") Long id);

    /**
     * 体检报告检查项信息分页列表
     *
     * @param page
     * @param query
     * @return
     */
    Paging<ReportItemVo> getReportItemPage(IPage page, @Param("query") ReportItemQuery query);

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
     * @param query 查询条件
     * @return
     */
    List<AppReportItemVo> getAppReportItemPage(@Param("query") AppReportItemQuery query);
    
    /**
     * 根据用户ID获取体检报告列表
     *
     * @param userId
     * @return
     */
    List<ReportItemVo> getReportItemsByUserId(@Param("userId") Long userId);
    
    /**
     * 更新report表中的report_item_ids字段
     *
     * @param reportId 报告ID
     * @param reportItemIds 报告项ID列表字符串
     * @return 受影响的行数
     */
    @Update("UPDATE report SET report_item_ids = #{reportItemIds}, update_time = NOW() WHERE id = #{reportId}")
    int updateReportItemIds(@Param("reportId") Long reportId, @Param("reportItemIds") String reportItemIds);
}
