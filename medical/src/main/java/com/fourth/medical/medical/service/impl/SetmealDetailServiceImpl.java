package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.SetmealDetailDto;
import com.fourth.medical.medical.entity.SetmealDetail;
import com.fourth.medical.medical.mapper.SetmealDetailMapper;
import com.fourth.medical.medical.query.SetmealDetailQuery;
import com.fourth.medical.medical.service.SetmealDetailService;
import com.fourth.medical.medical.vo.SetmealDetailVo;
import com.fourth.medical.medical.query.AppSetmealDetailQuery;
import com.fourth.medical.medical.vo.AppSetmealDetailVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * 体检套餐明细 服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class SetmealDetailServiceImpl extends ServiceImpl<SetmealDetailMapper, SetmealDetail> implements SetmealDetailService {

    @Autowired
    private SetmealDetailMapper setmealDetailMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addSetmealDetail(SetmealDetailDto dto) {
        SetmealDetail setmealDetail = new SetmealDetail();
        BeanUtils.copyProperties(dto, setmealDetail);
        return save(setmealDetail);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSetmealDetail(SetmealDetailDto dto) {
        Long id = dto.getId();
        SetmealDetail setmealDetail = getById(id);
        if (setmealDetail == null) {
            throw new BusinessException("体检套餐明细不存在");
        }
        BeanUtils.copyProperties(dto, setmealDetail);
        return updateById(setmealDetail);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSetmealDetail(Long id) {
        return removeById(id);
    }

    @Override
    public SetmealDetailVo getSetmealDetailById(Long id) {
        return setmealDetailMapper.getSetmealDetailById(id);
    }

    @Override
    public Paging<SetmealDetailVo> getSetmealDetailPage(SetmealDetailQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<SetmealDetailVo> list = setmealDetailMapper.getSetmealDetailPage(query);
        Paging<SetmealDetailVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppSetmealDetailVo getAppSetmealDetailById(Long id) {
        return setmealDetailMapper.getAppSetmealDetailById(id);
    }

    @Override
    public Paging<AppSetmealDetailVo> getAppSetmealDetailPage(AppSetmealDetailQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<AppSetmealDetailVo> list = setmealDetailMapper.getAppSetmealDetailPage(query);
        Paging<AppSetmealDetailVo> paging = new Paging<>(list);
        return paging;
    }

}
