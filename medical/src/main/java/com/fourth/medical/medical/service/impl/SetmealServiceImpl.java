package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.SetmealDto;
import com.fourth.medical.medical.entity.Setmeal;
import com.fourth.medical.medical.mapper.SetmealMapper;
import com.fourth.medical.medical.query.SetmealQuery;
import com.fourth.medical.medical.service.SetmealService;
import com.fourth.medical.medical.vo.SetmealVo;
import com.fourth.medical.medical.query.AppSetmealQuery;
import com.fourth.medical.medical.vo.AppSetmealVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.ArrayList;
import java.util.List;

/**
 * 体检套餐 服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addSetmeal(SetmealDto dto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(dto, setmeal);
        return save(setmeal);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSetmeal(SetmealDto dto) {
        Long id = dto.getId();
        Setmeal setmeal = getById(id);
        if (setmeal == null) {
            throw new BusinessException("体检套餐不存在");
        }
        BeanUtils.copyProperties(dto, setmeal);
        return updateById(setmeal);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSetmeal(Long id) {
        return removeById(id);
    }

    @Override
    public SetmealVo getSetmealById(Long id) {
        return setmealMapper.getSetmealById(id);
    }

    @Override
    public Paging<SetmealVo> getSetmealPage(SetmealQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<SetmealVo> list = setmealMapper.getSetmealPage(query);
        Paging<SetmealVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppSetmealVo getAppSetmealById(Long id) {
        return setmealMapper.getAppSetmealById(id);
    }

    @Override
    public Paging<AppSetmealVo> getAppSetmealPage(AppSetmealQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<AppSetmealVo> list = setmealMapper.getAppSetmealPage(query);
        Paging<AppSetmealVo> paging = new Paging<>(list);
        return paging;
    }
    
    @Override
    public List<AppSetmealVo> getRecommendSetmeals() {
        // 获取推荐的体检套餐，这里简单实现为获取状态为启用的前5个套餐
        LambdaQueryWrapper<Setmeal> wrapper = Wrappers.lambdaQuery(Setmeal.class)
            .eq(Setmeal::getStatus, true)
            .orderByDesc(Setmeal::getCreateTime)
            .last("limit 5");
            
        List<Setmeal> setmeals = list(wrapper);
        
        // 转换为AppSetmealVo列表
        List<AppSetmealVo> result = new ArrayList<>();
        for (Setmeal setmeal : setmeals) {
            AppSetmealVo vo = setmealMapper.getAppSetmealById(setmeal.getId());
            if (vo != null) {
                result.add(vo);
            }
        }
        
        return result;
    }

}
