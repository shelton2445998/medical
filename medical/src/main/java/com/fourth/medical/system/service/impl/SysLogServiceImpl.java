package com.fourth.medical.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.system.entity.SysLog;
import com.fourth.medical.system.mapper.SysLogMapper;
import com.fourth.medical.system.query.SysLogQuery;
import com.fourth.medical.system.service.SysLogService;
import com.fourth.medical.system.vo.SysLogVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统日志 服务实现类
 *
 * @author geekidea
 * @since 2023-02-16
 */
@Slf4j
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public SysLogVo getSysLogById(Long id) {
        return sysLogMapper.getSysLogById(id);
    }

    @Override
    public Paging<SysLogVo> getSysLogPage(SysLogQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<SysLogVo> list = sysLogMapper.getSysLogPage(query);
        Paging<SysLogVo> paging = new Paging<>(list);
        return paging;
    }

}
