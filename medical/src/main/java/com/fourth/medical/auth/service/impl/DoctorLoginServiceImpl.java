package com.fourth.medical.auth.service.impl;

import com.fourth.medical.auth.dto.DoctorLoginDto;
import com.fourth.medical.auth.service.DoctorLoginService;
import com.fourth.medical.auth.service.LoginRedisService;
import com.fourth.medical.auth.util.LoginUtil;
import com.fourth.medical.auth.util.TokenUtil;
import com.fourth.medical.auth.vo.LoginTokenVo;
import com.fourth.medical.auth.vo.LoginVo;
import com.fourth.medical.common.enums.SystemType;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.exception.LoginException;
import com.fourth.medical.medical.entity.Doctor;
import com.fourth.medical.medical.mapper.DoctorMapper;
import com.fourth.medical.system.mapper.SysMenuMapper;
import com.fourth.medical.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 医生登录服务实现
 *
 * @author fourth
 * @date 2023/4/20
 **/
@Slf4j
@Service
public class DoctorLoginServiceImpl implements DoctorLoginService {

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private LoginRedisService loginRedisService;

    @Override
    public LoginTokenVo login(DoctorLoginDto dto) {
        String mobile = dto.getMobile();
        // 根据手机号查询医生信息
        Doctor doctor = doctorMapper.getDoctorByMobile(mobile);
        if (doctor == null) {
            throw new BusinessException("医生账号不存在");
        }
        // 医生ID
        Long doctorId = doctor.getId();
        // 校验密码
        String dbPassword = doctor.getPassword();
        String dbSalt = doctor.getSalt();
        String password = dto.getPassword();
        String encryptPassword = PasswordUtil.encrypt(password, dbSalt);
        if (!encryptPassword.equals(dbPassword)) {
            throw new BusinessException("手机号或密码错误");
        }
        // 检查医生状态是否启用
        Boolean status = doctor.getStatus();
        if (status == false) {
            throw new BusinessException("医生账号已禁用");
        }
        // 生成token
        String token = TokenUtil.generateDoctorToken(doctorId);
        // 刷新登录信息
        refreshLoginInfo(doctor, token, new Date());
        // 返回token
        LoginTokenVo loginTokenVo = new LoginTokenVo();
        loginTokenVo.setToken(token);
        return loginTokenVo;
    }

    @Override
    public LoginVo refreshLoginInfo(Doctor doctor, String token, Date loginTime) {
        // 医生ID
        Long doctorId = doctor.getId();
        // 校验医生状态
        Boolean status = doctor.getStatus();
        if (status == false) {
            throw new BusinessException("医生账号已禁用");
        }
        
        // 设置登录用户对象
        LoginVo loginVo = new LoginVo();
        // 从医生实体复制属性到LoginVo
        loginVo.setUserId(doctorId);
        loginVo.setUsername(doctor.getMobile());  // 使用手机号作为用户名
        loginVo.setNickname(doctor.getName());    // 使用医生姓名作为昵称
        loginVo.setLoginTime(loginTime);
        loginVo.setAdmin(false);
        loginVo.setRoleCode("doctor");            // 医生角色编码
        loginVo.setRoleName("医生");              // 医生角色名称
        
        // 设置医生特有属性
        loginVo.setEmail(doctor.getEmail());
        loginVo.setPhone(doctor.getMobile());
        
        // 系统类型 设置为医生端
        loginVo.setSystemType(SystemType.DOCTOR.getCode());
        
        // 设置医生权限编码
        List<String> permissions = getDoctorPermissions(doctorId);
        loginVo.setPermissions(permissions);
        
        // 保存登录信息到redis中
        loginRedisService.setLoginVo(token, loginVo);
        return loginVo;
    }

    @Override
    public LoginVo getLoginDoctorInfo() {
        LoginVo loginVo = LoginUtil.getLoginVo();
        if (loginVo == null) {
            throw new LoginException("请先登录");
        }
        // 根据用户ID获取医生信息
        Long doctorId = loginVo.getUserId();
        // 获取用户登录时间
        Date loginTime = loginVo.getLoginTime();
        Doctor doctor = doctorMapper.selectById(doctorId);
        if (doctor == null) {
            throw new BusinessException("医生信息不存在");
        }
        // 刷新登录信息
        String token = TokenUtil.getToken();
        loginVo = refreshLoginInfo(doctor, token, loginTime);
        return loginVo;
    }

    @Override
    public void logout() {
        try {
            // 获取token
            String token = TokenUtil.getToken();
            // 删除缓存
            loginRedisService.deleteLoginVo(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取医生权限编码列表
     * 
     * @param doctorId 医生ID
     * @return 权限编码列表
     */
    private List<String> getDoctorPermissions(Long doctorId) {
        // 这里可以根据实际情况从数据库获取医生权限
        // 现在简化处理，直接返回医生固定权限
        List<String> permissions = new ArrayList<>();
        permissions.add("doctor:info");
        permissions.add("doctor:schedule");
        permissions.add("doctor:patient");
        permissions.add("doctor:prescription");
        return permissions;
    }
} 