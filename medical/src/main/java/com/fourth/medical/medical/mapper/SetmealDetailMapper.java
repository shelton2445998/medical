package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.SetmealDetail;
import com.fourth.medical.medical.query.SetmealDetailQuery;
import com.fourth.medical.medical.vo.SetmealDetailVo;
import com.fourth.medical.medical.query.AppSetmealDetailQuery;
import com.fourth.medical.medical.vo.AppSetmealDetailVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 体检套餐明细 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-09
 */
@Mapper
public interface SetmealDetailMapper extends BaseMapper<SetmealDetail> {

    /**
     * 体检套餐明细详情
     *
     * @param id
     * @return
     */
    SetmealDetailVo getSetmealDetailById(Long id);

    /**
     * 体检套餐明细分页列表
     *
     * @param query
     * @return
     */
    List<SetmealDetailVo> getSetmealDetailPage(SetmealDetailQuery query);

    /**
     * App体检套餐明细详情
     *
     * @param id
     * @return
     */
    AppSetmealDetailVo getAppSetmealDetailById(Long id);

    /**
     * App体检套餐明细分页列表
     *
     * @param query
     * @return
     */
    List<AppSetmealDetailVo> getAppSetmealDetailPage(AppSetmealDetailQuery query);

}
