package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.OverallResultDto;
import com.fourth.medical.medical.entity.OverallResult;
import com.fourth.medical.medical.mapper.OverallResultMapper;
import com.fourth.medical.medical.query.OverallResultQuery;
import com.fourth.medical.medical.service.OverallResultService;
import com.fourth.medical.medical.vo.OverallResultVo;
import com.fourth.medical.medical.query.AppOverallResultQuery;
import com.fourth.medical.medical.vo.AppOverallResultVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * 总检结论 服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class OverallResultServiceImpl extends ServiceImpl<OverallResultMapper, OverallResult> implements OverallResultService {

    @Autowired
    private OverallResultMapper overallResultMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addOverallResult(OverallResultDto dto) {
        OverallResult overallResult = new OverallResult();
        BeanUtils.copyProperties(dto, overallResult);
        return save(overallResult);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateOverallResult(OverallResultDto dto) {
        Long id = dto.getId();
        OverallResult overallResult = getById(id);
        if (overallResult == null) {
            throw new BusinessException("总检结论不存在");
        }
        BeanUtils.copyProperties(dto, overallResult);
        return updateById(overallResult);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteOverallResult(Long id) {
        return removeById(id);
    }

    @Override
    public OverallResultVo getOverallResultById(Long id) {
        return overallResultMapper.getOverallResultById(id);
    }

    @Override
    public Paging<OverallResultVo> getOverallResultPage(OverallResultQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<OverallResultVo> list = overallResultMapper.getOverallResultPage(query);
        Paging<OverallResultVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppOverallResultVo getAppOverallResultById(Long id) {
        return overallResultMapper.getAppOverallResultById(id);
    }

    @Override
    public Paging<AppOverallResultVo> getAppOverallResultPage(AppOverallResultQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<AppOverallResultVo> list = overallResultMapper.getAppOverallResultPage(query);
        Paging<AppOverallResultVo> paging = new Paging<>(list);
        return paging;
    }

}
