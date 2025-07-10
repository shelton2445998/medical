package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.OverallResultDto;
import com.fourth.medical.medical.entity.OverallResult;
import com.fourth.medical.medical.query.OverallResultQuery;
import com.fourth.medical.medical.vo.OverallResultVo;
import com.fourth.medical.medical.query.AppOverallResultQuery;
import com.fourth.medical.medical.vo.AppOverallResultVo;


/**
 * 总检结论 服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface OverallResultService extends IService<OverallResult> {

    /**
     * 添加总检结论
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addOverallResult(OverallResultDto dto);

    /**
     * 修改总检结论
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateOverallResult(OverallResultDto dto);

    /**
     * 删除总检结论
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteOverallResult(Long id);

    /**
     * 总检结论详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    OverallResultVo getOverallResultById(Long id);

    /**
     * 总检结论分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<OverallResultVo> getOverallResultPage(OverallResultQuery query);

    /**
     * App总检结论详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppOverallResultVo getAppOverallResultById(Long id);

    /**
     * App总检结论分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppOverallResultVo> getAppOverallResultPage(AppOverallResultQuery query);

}
