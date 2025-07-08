package com.forth.medicalreserve.service.impl;

import com.forth.medicalreserve.entity.Users;
import com.forth.medicalreserve.repository.UsersRepository;
import com.forth.medicalreserve.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public Users register(Users user) {
        // 校验用户名是否存在
        if (usersRepository.existsByUserName(user.getUserName())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 校验手机号是否存在
        if (usersRepository.existsByMobile(user.getMobile())) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 设置状态为启用
        user.setStatus(1);
        // 设置创建时间和更新时间
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        
        return usersRepository.save(user);
    }

    @Override
    public Users login(String userName, String password) {
        Optional<Users> userOpt = usersRepository.findByUserName(userName);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户名不存在");
        }
        
        Users user = userOpt.get();
        // 校验密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        // 校验用户状态
        if (user.getStatus() != 1) {
            throw new RuntimeException("用户已被禁用");
        }
        
        return user;
    }

    @Override
    public Users loginByMobile(String mobile, String code) {
        // TODO 校验验证码
        
        Optional<Users> userOpt = usersRepository.findByMobile(mobile);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("手机号未注册");
        }
        
        Users user = userOpt.get();
        // 校验用户状态
        if (user.getStatus() != 1) {
            throw new RuntimeException("用户已被禁用");
        }
        
        return user;
    }

    @Override
    public Users findById(Integer userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    @Override
    @Transactional
    public Users updateUser(Users user) {
        Users existingUser = findById(user.getUserId());
        
        // 更新基本信息
        existingUser.setRealName(user.getRealName());
        existingUser.setGender(user.getGender());
        existingUser.setEmail(user.getEmail());
        existingUser.setAddress(user.getAddress());
        existingUser.setUpdateTime(new Date());
        
        return usersRepository.save(existingUser);
    }

    @Override
    @Transactional
    public boolean changePassword(Integer userId, String oldPassword, String newPassword) {
        Users user = findById(userId);
        
        // 校验旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(new Date());
        usersRepository.save(user);
        
        return true;
    }

    @Override
    @Transactional
    public boolean resetPassword(String mobile, String code, String newPassword) {
        // TODO 校验验证码
        
        Optional<Users> userOpt = usersRepository.findByMobile(mobile);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("手机号未注册");
        }
        
        Users user = userOpt.get();
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(new Date());
        usersRepository.save(user);
        
        return true;
    }

    @Override
    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    @Override
    @Transactional
    public boolean updateStatus(Integer userId, Integer status) {
        Users user = findById(userId);
        user.setStatus(status);
        user.setUpdateTime(new Date());
        usersRepository.save(user);
        return true;
    }
} 