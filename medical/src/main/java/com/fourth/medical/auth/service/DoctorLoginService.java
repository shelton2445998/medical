package com.fourth.medical.auth.service;

import com.fourth.medical.auth.dto.DoctorLoginDto;
import com.fourth.medical.auth.dto.DoctorUpdatePasswordDto;
import com.fourth.medical.auth.vo.LoginTokenVo;
import com.fourth.medical.auth.vo.LoginVo;
import com.fourth.medical.medical.dto.DoctorUpdateProfileDto;
import com.fourth.medical.medical.entity.Doctor;

import java.util.Date;

/**
 * 医生登录服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface DoctorLoginService {

    /**
     * 医生登录
     *
     * @param doctorLoginDto
     * @return
     * @throws Exception
     */
    LoginTokenVo login(DoctorLoginDto doctorLoginDto);

    /**
     * 处理登录用户信息
     *
     * @param doctor 医生实体
     * @param token
     * @param loginTime
     * @return
     */
    LoginVo refreshLoginInfo(Doctor doctor, String token, Date loginTime);

    /**
     * 获取登录医生信息
     *
     * @return
     * @throws Exception
     */
    LoginVo getLoginDoctorInfo();

    /**
     * 医生登出
     *
     * @throws Exception
     */
    void logout();

    /**
     * 修改医生密码
     *
     * @param dto 密码修改信息
     * @return 是否修改成功
     * @throws Exception
     */
    boolean updatePassword(DoctorUpdatePasswordDto dto);

    /**
     * 修改个人信息
     *
     * @param dto 个人信息
     * @return 是否修改成功
     * @throws Exception
     */
    boolean updateProfile(DoctorUpdateProfileDto dto);

} 