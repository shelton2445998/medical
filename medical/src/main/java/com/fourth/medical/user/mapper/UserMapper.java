package com.fourth.medical.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.user.entity.User;
import com.fourth.medical.user.query.UserQuery;
import com.fourth.medical.user.vo.UserVo;
import com.fourth.medical.user.query.AppUserQuery;
import com.fourth.medical.user.vo.AppUserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户信息 Mapper 接口
 *
 * @author geekidea
 * @since 2023-11-25
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 用户信息详情
     *
     * @param id
     * @return
     */
    UserVo getUserById(Long id);

    /**
     * 用户信息分页列表
     *
     * @param query
     * @return
     */
    List<UserVo> getUserPage(UserQuery query);

    /**
     * App用户信息详情
     *
     * @param id
     * @return
     */
    AppUserVo getAppUserById(Long id);

    /**
     * App用户信息分页列表
     *
     * @param query
     * @return
     */
    List<AppUserVo> getAppUserPage(AppUserQuery query);

}
