package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.FamilyMemberDto;
import com.fourth.medical.medical.entity.FamilyMember;
import com.fourth.medical.medical.query.FamilyMemberQuery;
import com.fourth.medical.medical.vo.FamilyMemberVo;
import com.fourth.medical.medical.query.AppFamilyMemberQuery;
import com.fourth.medical.medical.vo.AppFamilyMemberVo;


/**
 * 家庭成员 服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface FamilyMemberService extends IService<FamilyMember> {

    /**
     * 添加家庭成员
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addFamilyMember(FamilyMemberDto dto);

    /**
     * 修改家庭成员
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateFamilyMember(FamilyMemberDto dto);

    /**
     * 删除家庭成员
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteFamilyMember(Long id);

    /**
     * 家庭成员详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    FamilyMemberVo getFamilyMemberById(Long id);

    /**
     * 家庭成员分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<FamilyMemberVo> getFamilyMemberPage(FamilyMemberQuery query);

    /**
     * App家庭成员详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppFamilyMemberVo getAppFamilyMemberById(Long id);

    /**
     * App家庭成员分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppFamilyMemberVo> getAppFamilyMemberPage(AppFamilyMemberQuery query);

}
