package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.CheckitemDto;
import com.fourth.medical.medical.entity.Checkitem;
import com.fourth.medical.medical.query.CheckitemQuery;
import com.fourth.medical.medical.vo.CheckitemVo;
import com.fourth.medical.medical.query.AppCheckitemQuery;
import com.fourth.medical.medical.vo.AppCheckitemVo;


/**
 * 检查项 服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface CheckitemService extends IService<Checkitem> {

    /**
     * 添加检查项
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addCheckitem(CheckitemDto dto);

    /**
     * 修改检查项
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateCheckitem(CheckitemDto dto);

    /**
     * 删除检查项
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteCheckitem(Long id);

    /**
     * 检查项详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    CheckitemVo getCheckitemById(Long id);

    /**
     * 检查项分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<CheckitemVo> getCheckitemPage(CheckitemQuery query);

    /**
     * App检查项详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppCheckitemVo getAppCheckitemById(Long id);

    /**
     * App检查项分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppCheckitemVo> getAppCheckitemPage(AppCheckitemQuery query);

    /**
     * 根据检查项ID列表获取部门信息
     *
     * @param checkitemIds 检查项ID列表，逗号分隔
     * @return 部门信息
     * @throws Exception
     */
    Object getDepartmentByCheckitemIds(String checkitemIds);

}
