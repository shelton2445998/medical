package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.CheckitemDto;
import com.fourth.medical.medical.entity.Checkitem;
import com.fourth.medical.medical.mapper.CheckitemMapper;
import com.fourth.medical.medical.query.CheckitemQuery;
import com.fourth.medical.medical.service.CheckitemService;
import com.fourth.medical.medical.vo.CheckitemVo;
import com.fourth.medical.medical.query.AppCheckitemQuery;
import com.fourth.medical.medical.vo.AppCheckitemVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * 检查项 服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class CheckitemServiceImpl extends ServiceImpl<CheckitemMapper, Checkitem> implements CheckitemService {

    @Autowired
    private CheckitemMapper checkitemMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addCheckitem(CheckitemDto dto) {
        Checkitem checkitem = new Checkitem();
        BeanUtils.copyProperties(dto, checkitem);
        return save(checkitem);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateCheckitem(CheckitemDto dto) {
        Long id = dto.getId();
        Checkitem checkitem = getById(id);
        if (checkitem == null) {
            throw new BusinessException("检查项不存在");
        }
        BeanUtils.copyProperties(dto, checkitem);
        return updateById(checkitem);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteCheckitem(Long id) {
        return removeById(id);
    }

    @Override
    public CheckitemVo getCheckitemById(Long id) {
        return checkitemMapper.getCheckitemById(id);
    }

    @Override
    public Paging<CheckitemVo> getCheckitemPage(CheckitemQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<CheckitemVo> list = checkitemMapper.getCheckitemPage(query);
        Paging<CheckitemVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppCheckitemVo getAppCheckitemById(Long id) {
        return checkitemMapper.getAppCheckitemById(id);
    }

    @Override
    public Paging<AppCheckitemVo> getAppCheckitemPage(AppCheckitemQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<AppCheckitemVo> list = checkitemMapper.getAppCheckitemPage(query);
        Paging<AppCheckitemVo> paging = new Paging<>(list);
        return paging;
    }

}
