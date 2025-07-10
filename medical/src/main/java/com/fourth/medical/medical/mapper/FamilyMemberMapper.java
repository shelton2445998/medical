package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.FamilyMember;
import com.fourth.medical.medical.query.FamilyMemberQuery;
import com.fourth.medical.medical.vo.FamilyMemberVo;
import com.fourth.medical.medical.query.AppFamilyMemberQuery;
import com.fourth.medical.medical.vo.AppFamilyMemberVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 家庭成员 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-09
 */
@Mapper
public interface FamilyMemberMapper extends BaseMapper<FamilyMember> {

    /**
     * 家庭成员详情
     *
     * @param id
     * @return
     */
    FamilyMemberVo getFamilyMemberById(Long id);

    /**
     * 家庭成员分页列表
     *
     * @param query
     * @return
     */
    List<FamilyMemberVo> getFamilyMemberPage(FamilyMemberQuery query);

    /**
     * App家庭成员详情
     *
     * @param id
     * @return
     */
    AppFamilyMemberVo getAppFamilyMemberById(Long id);

    /**
     * App家庭成员分页列表
     *
     * @param query
     * @return
     */
    List<AppFamilyMemberVo> getAppFamilyMemberPage(AppFamilyMemberQuery query);

}
