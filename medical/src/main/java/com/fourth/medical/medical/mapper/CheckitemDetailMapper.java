package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.CheckitemDetail;
import com.fourth.medical.medical.query.CheckitemDetailQuery;
import com.fourth.medical.medical.vo.CheckitemDetailVo;
import com.fourth.medical.medical.query.AppCheckitemDetailQuery;
import com.fourth.medical.medical.vo.AppCheckitemDetailVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 检查项明细 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-09
 */
@Mapper
public interface CheckitemDetailMapper extends BaseMapper<CheckitemDetail> {

    /**
     * 检查项明细详情
     *
     * @param id
     * @return
     */
    CheckitemDetailVo getCheckitemDetailById(Long id);

    /**
     * 检查项明细分页列表
     *
     * @param query
     * @return
     */
    List<CheckitemDetailVo> getCheckitemDetailPage(CheckitemDetailQuery query);

    /**
     * App检查项明细详情
     *
     * @param id
     * @return
     */
    AppCheckitemDetailVo getAppCheckitemDetailById(Long id);

    /**
     * App检查项明细分页列表
     *
     * @param query
     * @return
     */
    List<AppCheckitemDetailVo> getAppCheckitemDetailPage(AppCheckitemDetailQuery query);

}
