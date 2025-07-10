package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.FamilyMemberDto;
import com.fourth.medical.medical.entity.FamilyMember;
import com.fourth.medical.medical.mapper.FamilyMemberMapper;
import com.fourth.medical.medical.query.FamilyMemberQuery;
import com.fourth.medical.medical.service.FamilyMemberService;
import com.fourth.medical.medical.vo.FamilyMemberVo;
import com.fourth.medical.medical.query.AppFamilyMemberQuery;
import com.fourth.medical.medical.vo.AppFamilyMemberVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * 家庭成员 服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class FamilyMemberServiceImpl extends ServiceImpl<FamilyMemberMapper, FamilyMember> implements FamilyMemberService {

    @Autowired
    private FamilyMemberMapper familyMemberMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addFamilyMember(FamilyMemberDto dto) {
        FamilyMember familyMember = new FamilyMember();
        BeanUtils.copyProperties(dto, familyMember);
        return save(familyMember);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateFamilyMember(FamilyMemberDto dto) {
        Long id = dto.getId();
        FamilyMember familyMember = getById(id);
        if (familyMember == null) {
            throw new BusinessException("家庭成员不存在");
        }
        BeanUtils.copyProperties(dto, familyMember);
        return updateById(familyMember);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteFamilyMember(Long id) {
        return removeById(id);
    }

    @Override
    public FamilyMemberVo getFamilyMemberById(Long id) {
        return familyMemberMapper.getFamilyMemberById(id);
    }

    @Override
    public Paging<FamilyMemberVo> getFamilyMemberPage(FamilyMemberQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<FamilyMemberVo> list = familyMemberMapper.getFamilyMemberPage(query);
        Paging<FamilyMemberVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppFamilyMemberVo getAppFamilyMemberById(Long id) {
        return familyMemberMapper.getAppFamilyMemberById(id);
    }

    @Override
    public Paging<AppFamilyMemberVo> getAppFamilyMemberPage(AppFamilyMemberQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<AppFamilyMemberVo> list = familyMemberMapper.getAppFamilyMemberPage(query);
        Paging<AppFamilyMemberVo> paging = new Paging<>(list);
        return paging;
    }

}
