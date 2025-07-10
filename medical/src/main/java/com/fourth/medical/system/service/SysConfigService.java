package com.fourth.medical.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.system.dto.SysConfigDto;
import com.fourth.medical.system.entity.SysConfig;
import com.fourth.medical.system.query.SysConfigQuery;
import com.fourth.medical.system.vo.SysConfigVo;


/**
 * 系统配置 服务接口
 *
 * @author geekidea
 * @since 2023-11-27
 */
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 添加系统配置
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addSysConfig(SysConfigDto dto);

    /**
     * 修改系统配置
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateSysConfig(SysConfigDto dto);

    /**
     * 删除系统配置
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSysConfig(Long id);

    /**
     * 系统配置详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    SysConfigVo getSysConfigById(Long id);

    /**
     * 系统配置分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<SysConfigVo> getSysConfigPage(SysConfigQuery query);

    /**
     * 检查configKey是否存在
     *
     * @param configKey
     * @return
     * @throws Exception
     */
    void checkConfigKeyExists(String configKey);

}
