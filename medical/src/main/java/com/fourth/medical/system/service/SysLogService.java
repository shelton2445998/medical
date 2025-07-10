package com.fourth.medical.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.system.entity.SysLog;
import com.fourth.medical.system.query.SysLogQuery;
import com.fourth.medical.system.vo.SysLogVo;

/**
 * 系统日志 服务接口
 *
 * @author geekidea
 * @since 2023-02-16
 */
public interface SysLogService extends IService<SysLog> {

    /**
     * 系统日志详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    SysLogVo getSysLogById(Long id);

    /**
     * 系统日志分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<SysLogVo> getSysLogPage(SysLogQuery query);

}
