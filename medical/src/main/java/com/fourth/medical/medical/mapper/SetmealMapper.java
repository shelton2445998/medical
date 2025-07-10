package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.Setmeal;
import com.fourth.medical.medical.query.SetmealQuery;
import com.fourth.medical.medical.vo.SetmealVo;
import com.fourth.medical.medical.query.AppSetmealQuery;
import com.fourth.medical.medical.vo.AppSetmealVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 体检套餐 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-09
 */
@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {

    /**
     * 体检套餐详情
     *
     * @param id
     * @return
     */
    SetmealVo getSetmealById(Long id);

    /**
     * 体检套餐分页列表
     *
     * @param query
     * @return
     */
    List<SetmealVo> getSetmealPage(SetmealQuery query);

    /**
     * App体检套餐详情
     *
     * @param id
     * @return
     */
    AppSetmealVo getAppSetmealById(Long id);

    /**
     * App体检套餐分页列表
     *
     * @param query
     * @return
     */
    List<AppSetmealVo> getAppSetmealPage(AppSetmealQuery query);

}
