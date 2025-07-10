package com.fourth.medical.auth.service;

import com.fourth.medical.auth.dto.LoginDto;
import com.fourth.medical.auth.vo.LoginTokenVo;
import com.fourth.medical.auth.vo.LoginVo;
import com.fourth.medical.system.entity.SysUser;

import java.util.Date;

/**
 * @author geekidea
 * @date 2022/7/5
 **/
public interface LoginService {

    /**
     * 登录
     *
     * @param dto
     * @return
     * @throws Exception
     */
    LoginTokenVo login(LoginDto dto);

    /**
     * 处理登录用户信息
     *
     * @param sysUser
     * @param token
     * @param loginTime
     * @return
     * @throws Exception
     */
    LoginVo refreshLoginInfo(SysUser sysUser, String token, Date loginTime);

    /**
     * 获取登录用户信息
     *
     * @return
     * @throws Exception
     */
    LoginVo getLoginUserInfo();

    /**
     * 登出
     *
     * @throws Exception
     */
    void logout();
    
    /**
     * 用户注册
     *
     * @param dto
     * @return
     * @throws Exception
     */
    LoginTokenVo register(LoginDto dto);

}
