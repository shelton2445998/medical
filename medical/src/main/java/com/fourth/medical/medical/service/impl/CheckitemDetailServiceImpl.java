package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.CheckitemDetailDto;
import com.fourth.medical.medical.entity.CheckitemDetail;
import com.fourth.medical.medical.mapper.CheckitemDetailMapper;
import com.fourth.medical.medical.query.CheckitemDetailQuery;
import com.fourth.medical.medical.service.CheckitemDetailService;
import com.fourth.medical.medical.vo.CheckitemDetailVo;
import com.fourth.medical.medical.query.AppCheckitemDetailQuery;
import com.fourth.medical.medical.vo.AppCheckitemDetailVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * 检查项明细 服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class CheckitemDetailServiceImpl extends ServiceImpl<CheckitemDetailMapper, CheckitemDetail> implements CheckitemDetailService {

    @Autowired
    private CheckitemDetailMapper checkitemDetailMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addCheckitemDetail(CheckitemDetailDto dto) {
        CheckitemDetail checkitemDetail = new CheckitemDetail();
        BeanUtils.copyProperties(dto, checkitemDetail);
        return save(checkitemDetail);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateCheckitemDetail(CheckitemDetailDto dto) {
        Long id = dto.getId();
        CheckitemDetail checkitemDetail = getById(id);
        if (checkitemDetail == null) {
            throw new BusinessException("检查项明细不存在");
        }
        BeanUtils.copyProperties(dto, checkitemDetail);
        return updateById(checkitemDetail);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteCheckitemDetail(Long id) {
        return removeById(id);
    }

    @Override
    public CheckitemDetailVo getCheckitemDetailById(Long id) {
        return checkitemDetailMapper.getCheckitemDetailById(id);
    }

    @Override
    public Paging<CheckitemDetailVo> getCheckitemDetailPage(CheckitemDetailQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<CheckitemDetailVo> list = checkitemDetailMapper.getCheckitemDetailPage(query);
        Paging<CheckitemDetailVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppCheckitemDetailVo getAppCheckitemDetailById(Long id) {
        return checkitemDetailMapper.getAppCheckitemDetailById(id);
    }

    @Override
    public Paging<AppCheckitemDetailVo> getAppCheckitemDetailPage(AppCheckitemDetailQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<AppCheckitemDetailVo> list = checkitemDetailMapper.getAppCheckitemDetailPage(query);
        Paging<AppCheckitemDetailVo> paging = new Paging<>(list);
        return paging;
    }

}
