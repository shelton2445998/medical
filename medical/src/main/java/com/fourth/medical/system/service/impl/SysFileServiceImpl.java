package com.fourth.medical.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.system.dto.SysFileDto;
import com.fourth.medical.system.entity.SysFile;
import com.fourth.medical.system.mapper.SysFileMapper;
import com.fourth.medical.system.query.SysFileQuery;
import com.fourth.medical.system.service.SysFileService;
import com.fourth.medical.system.vo.SysFileVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统文件 服务实现类
 *
 * @author geekidea
 * @since 2023-11-26
 */
@Slf4j
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {

    @Autowired
    private SysFileMapper sysFileMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSysFile(SysFileDto dto) {
        Long id = dto.getId();
        SysFile sysFile = getById(id);
        if (sysFile == null) {
            throw new BusinessException("系统文件不存在");
        }
        BeanUtils.copyProperties(dto, sysFile);
        return updateById(sysFile);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSysFile(Long id) {
        return removeById(id);
    }

    @Override
    public SysFileVo getSysFileById(Long id) {
        return sysFileMapper.getSysFileById(id);
    }

    @Override
    public Paging<SysFileVo> getSysFilePage(SysFileQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<SysFileVo> list = sysFileMapper.getSysFilePage(query);
        Paging<SysFileVo> paging = new Paging<>(list);
        return paging;
    }

}
