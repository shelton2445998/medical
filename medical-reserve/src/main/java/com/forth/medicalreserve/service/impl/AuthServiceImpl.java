package com.forth.medicalreserve.service.impl;

import com.forth.medicalreserve.constant.CommonConstant;
import com.forth.medicalreserve.dto.LoginRequest;
import com.forth.medicalreserve.dto.LoginResponse;
import com.forth.medicalreserve.dto.RegisterDTO;
import com.forth.medicalreserve.entity.Admin;
import com.forth.medicalreserve.entity.Doctor;
import com.forth.medicalreserve.entity.User;
import com.forth.medicalreserve.exception.BusinessException;
import com.forth.medicalreserve.repository.AdminRepository;
import com.forth.medicalreserve.repository.DoctorRepository;
import com.forth.medicalreserve.repository.UserRepository;
import com.forth.medicalreserve.service.AuthService;
import com.forth.medicalreserve.util.JwtTokenUtil;
import com.forth.medicalreserve.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String userType = loginRequest.getUserType();

        switch (userType.toUpperCase()) {
            case "USER":
                return loginUser(username, password);
            case "ADMIN":
                return loginAdmin(username, password);
            case "DOCTOR":
                return loginDoctor(username, password);
            default:
                throw new BusinessException("不支持的用户类型");
        }
    }

    @Override
    public void logout(String token) {
        // 验证token有效性
        if (!jwtTokenUtil.validateToken(token)) {
            return;
        }

        // 获取token中的用户类型
        String userType = jwtTokenUtil.getUserTypeFromToken(token);
        
        // 获取token中的用户ID
        Integer userId = jwtTokenUtil.getIdFromToken(token);

        // 根据用户类型获取Redis中的key前缀
        String keyPrefix;
        switch (userType.toUpperCase()) {
            case "USER":
                keyPrefix = CommonConstant.TOKEN_PREFIX;
                break;
            case "ADMIN":
                keyPrefix = CommonConstant.ADMIN_TOKEN_PREFIX;
                break;
            case "DOCTOR":
                keyPrefix = CommonConstant.DOCTOR_TOKEN_PREFIX;
                break;
            default:
                return;
        }

        // 从Redis中删除token
        redisTemplate.delete(keyPrefix + userId);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtTokenUtil.validateToken(token);
    }

    @Override
    @Transactional
    public Integer register(RegisterDTO registerDTO) {
        // 验证密码和确认密码是否一致
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }
        
        // 验证用户名是否已存在
        if (userRepository.existsByUserName(registerDTO.getUserName())) {
            throw new BusinessException("用户名已存在");
        }
        
        // 验证手机号是否已存在
        if (userRepository.existsByMobile(registerDTO.getMobile())) {
            throw new BusinessException("手机号已被使用");
        }
        
        // 创建用户
        User user = new User();
        user.setUserName(registerDTO.getUserName());
        user.setPassword(MD5Util.encrypt(registerDTO.getPassword()));
        user.setMobile(registerDTO.getMobile());
        user.setRealName(registerDTO.getRealName());
        user.setGender(registerDTO.getGender());
        user.setStatus(1);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        
        // 保存用户
        User savedUser = userRepository.save(user);
        return savedUser.getUserId();
    }

    /**
     * 用户登录
     */
    private LoginResponse loginUser(String username, String password) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));

        if (!MD5Util.verify(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用，请联系管理员");
        }

        // 生成JWT
        String token = jwtTokenUtil.generateToken(user.getUserId(), user.getUserName(), "USER");

        // 将token存储到Redis中
        redisTemplate.opsForValue().set(
                CommonConstant.TOKEN_PREFIX + user.getUserId(),
                token,
                CommonConstant.TOKEN_EXPIRE_HOURS,
                TimeUnit.HOURS
        );

        return new LoginResponse(
                user.getUserId(),
                user.getUserName(),
                user.getRealName(),
                "USER",
                token
        );
    }

    /**
     * 管理员登录
     */
    private LoginResponse loginAdmin(String username, String password) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));

        if (!MD5Util.verify(password, admin.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 检查管理员状态
        if (admin.getStatus() == 0) {
            throw new BusinessException("账号已被禁用，请联系超级管理员");
        }

        // 生成JWT
        String token = jwtTokenUtil.generateToken(admin.getAdminId(), admin.getUsername(), "ADMIN");

        // 将token存储到Redis中
        redisTemplate.opsForValue().set(
                CommonConstant.ADMIN_TOKEN_PREFIX + admin.getAdminId(),
                token,
                CommonConstant.TOKEN_EXPIRE_HOURS,
                TimeUnit.HOURS
        );

        return new LoginResponse(
                admin.getAdminId(),
                admin.getUsername(),
                admin.getRealName(),
                admin.getRole(),
                token
        );
    }

    /**
     * 医生登录
     */
    private LoginResponse loginDoctor(String username, String password) {
        Doctor doctor = doctorRepository.findByDoctorName(username)
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));

        if (!MD5Util.verify(password, doctor.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 检查医生状态
        if (doctor.getStatus() == 0) {
            throw new BusinessException("账号已被禁用，请联系管理员");
        }

        // 生成JWT
        String token = jwtTokenUtil.generateToken(doctor.getDoctorId(), doctor.getDoctorName(), "DOCTOR");

        // 将token存储到Redis中
        redisTemplate.opsForValue().set(
                CommonConstant.DOCTOR_TOKEN_PREFIX + doctor.getDoctorId(),
                token,
                CommonConstant.TOKEN_EXPIRE_HOURS,
                TimeUnit.HOURS
        );

        return new LoginResponse(
                doctor.getDoctorId(),
                doctor.getDoctorName(),
                doctor.getDoctorName(),
                "DOCTOR",
                token
        );
    }
} 