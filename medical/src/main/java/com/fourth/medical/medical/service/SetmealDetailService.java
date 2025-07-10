package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.SetmealDetailDto;
import com.fourth.medical.medical.entity.SetmealDetail;
import com.fourth.medical.medical.query.SetmealDetailQuery;
import com.fourth.medical.medical.vo.SetmealDetailVo;
import com.fourth.medical.medical.query.AppSetmealDetailQuery;
import com.fourth.medical.medical.vo.AppSetmealDetailVo;


/**
 * 体检套餐明细 服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface SetmealDetailService extends IService<SetmealDetail> {

    /**
     * 添加体检套餐明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addSetmealDetail(SetmealDetailDto dto);

    /**
     * 修改体检套餐明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateSetmealDetail(SetmealDetailDto dto);

    /**
     * 删除体检套餐明细
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSetmealDetail(Long id);

    /**
     * 体检套餐明细详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    SetmealDetailVo getSetmealDetailById(Long id);

    /**
     * 体检套餐明细分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<SetmealDetailVo> getSetmealDetailPage(SetmealDetailQuery query);

    /**
     * App体检套餐明细详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppSetmealDetailVo getAppSetmealDetailById(Long id);

    /**
     * App体检套餐明细分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppSetmealDetailVo> getAppSetmealDetailPage(AppSetmealDetailQuery query);

}
