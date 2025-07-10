package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.SetmealDto;
import com.fourth.medical.medical.entity.Setmeal;
import com.fourth.medical.medical.query.SetmealQuery;
import com.fourth.medical.medical.vo.SetmealVo;
import com.fourth.medical.medical.query.AppSetmealQuery;
import com.fourth.medical.medical.vo.AppSetmealVo;

import java.util.List;


/**
 * 体检套餐 服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 添加体检套餐
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addSetmeal(SetmealDto dto);

    /**
     * 修改体检套餐
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateSetmeal(SetmealDto dto);

    /**
     * 删除体检套餐
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSetmeal(Long id);

    /**
     * 体检套餐详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    SetmealVo getSetmealById(Long id);

    /**
     * 体检套餐分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<SetmealVo> getSetmealPage(SetmealQuery query);

    /**
     * App体检套餐详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppSetmealVo getAppSetmealById(Long id);

    /**
     * App体检套餐分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppSetmealVo> getAppSetmealPage(AppSetmealQuery query);
    
    /**
     * 获取推荐体检套餐列表
     *
     * @return
     * @throws Exception
     */
    List<AppSetmealVo> getRecommendSetmeals();

}
