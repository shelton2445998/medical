package com.fourth.medical.auth.service;

import com.fourth.medical.auth.dto.DoctorLoginDto;
import com.fourth.medical.auth.vo.LoginTokenVo;
import com.fourth.medical.auth.vo.LoginVo;
import com.fourth.medical.medical.entity.Doctor;

import java.util.Date;

/**
 * 医生登录服务接口
 *
 * @author fourth
 * @date 2023/4/20
 **/
public interface DoctorLoginService {

    /**
     * 医生登录
     *
     * @param dto
     * @return
     */
    LoginTokenVo login(DoctorLoginDto dto);

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
     */
    LoginVo getLoginDoctorInfo();

    /**
     * 医生登出
     */
    void logout();
} 