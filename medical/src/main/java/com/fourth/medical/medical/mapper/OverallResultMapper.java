package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.OverallResult;
import com.fourth.medical.medical.query.OverallResultQuery;
import com.fourth.medical.medical.vo.OverallResultVo;
import com.fourth.medical.medical.query.AppOverallResultQuery;
import com.fourth.medical.medical.vo.AppOverallResultVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 总检结论 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-09
 */
@Mapper
public interface OverallResultMapper extends BaseMapper<OverallResult> {

    /**
     * 总检结论详情
     *
     * @param id
     * @return
     */
    OverallResultVo getOverallResultById(Long id);

    /**
     * 总检结论分页列表
     *
     * @param query
     * @return
     */
    List<OverallResultVo> getOverallResultPage(OverallResultQuery query);

    /**
     * App总检结论详情
     *
     * @param id
     * @return
     */
    AppOverallResultVo getAppOverallResultById(Long id);

    /**
     * App总检结论分页列表
     *
     * @param query
     * @return
     */
    List<AppOverallResultVo> getAppOverallResultPage(AppOverallResultQuery query);

}
