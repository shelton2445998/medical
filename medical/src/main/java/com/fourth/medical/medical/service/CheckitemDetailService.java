package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.CheckitemDetailDto;
import com.fourth.medical.medical.entity.CheckitemDetail;
import com.fourth.medical.medical.query.CheckitemDetailQuery;
import com.fourth.medical.medical.vo.CheckitemDetailVo;
import com.fourth.medical.medical.query.AppCheckitemDetailQuery;
import com.fourth.medical.medical.vo.AppCheckitemDetailVo;


/**
 * 检查项明细 服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface CheckitemDetailService extends IService<CheckitemDetail> {

    /**
     * 添加检查项明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addCheckitemDetail(CheckitemDetailDto dto);

    /**
     * 修改检查项明细
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateCheckitemDetail(CheckitemDetailDto dto);

    /**
     * 删除检查项明细
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteCheckitemDetail(Long id);

    /**
     * 检查项明细详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    CheckitemDetailVo getCheckitemDetailById(Long id);

    /**
     * 检查项明细分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<CheckitemDetailVo> getCheckitemDetailPage(CheckitemDetailQuery query);

    /**
     * App检查项明细详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppCheckitemDetailVo getAppCheckitemDetailById(Long id);

    /**
     * App检查项明细分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppCheckitemDetailVo> getAppCheckitemDetailPage(AppCheckitemDetailQuery query);

}
