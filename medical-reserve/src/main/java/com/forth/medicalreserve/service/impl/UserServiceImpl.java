package com.forth.medicalreserve.service.impl;

import com.forth.medicalreserve.dto.UserDTO;
import com.forth.medicalreserve.entity.User;
import com.forth.medicalreserve.exception.BusinessException;
import com.forth.medicalreserve.repository.UserRepository;
import com.forth.medicalreserve.service.UserService;
import com.forth.medicalreserve.util.MD5Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }

    @Override
    public User getByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }

    @Override
    public User getByMobile(String mobile) {
        return userRepository.findByMobile(mobile)
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }

    @Override
    public Page<User> page(int pageNum, int pageSize, String keyword) {
        // 构建查询条件
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // 模糊查询
                .withIgnoreCase(true); // 忽略大小写

        User user = new User();
        if (StringUtils.hasText(keyword)) {
            user.setUserName(keyword);
            // 也可以设置其他字段进行查询
        }

        Example<User> example = Example.of(user, matcher);
        return userRepository.findAll(example, PageRequest.of(pageNum - 1, pageSize));
    }

    @Override
    @Transactional
    public Integer create(UserDTO userDTO) {
        // 检查用户名是否已存在
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            throw new BusinessException("用户名已存在");
        }

        // 检查手机号是否已存在
        if (userRepository.existsByMobile(userDTO.getMobile())) {
            throw new BusinessException("手机号已被使用");
        }

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        // 设置密码（MD5加密）
        user.setPassword(MD5Util.encrypt(userDTO.getPassword()));

        // 设置状态和时间
        user.setStatus(1);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        // 保存用户并返回用户ID
        User savedUser = userRepository.save(user);
        return savedUser.getUserId();
    }

    @Override
    @Transactional
    public void update(Integer userId, UserDTO userDTO) {
        // 获取用户信息
        User user = getById(userId);

        // 如果修改了用户名，检查是否已存在
        if (!user.getUserName().equals(userDTO.getUserName()) 
                && userRepository.existsByUserName(userDTO.getUserName())) {
            throw new BusinessException("用户名已存在");
        }

        // 如果修改了手机号，检查是否已存在
        if (!user.getMobile().equals(userDTO.getMobile()) 
                && userRepository.existsByMobile(userDTO.getMobile())) {
            throw new BusinessException("手机号已被使用");
        }

        // 复制属性（不包括密码、状态等字段）
        BeanUtils.copyProperties(userDTO, user, "password", "status", "createTime", "updateTime");

        // 如果设置了新密码，则更新密码
        if (StringUtils.hasText(userDTO.getPassword())) {
            user.setPassword(MD5Util.encrypt(userDTO.getPassword()));
        }

        // 更新时间
        user.setUpdateTime(new Date());

        // 保存用户
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Integer userId) {
        // 检查用户是否存在
        if (!userRepository.existsById(userId)) {
            throw new BusinessException("用户不存在");
        }

        // 删除用户
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public void updateStatus(Integer userId, Integer status) {
        // 获取用户信息
        User user = getById(userId);

        // 更新状态
        user.setStatus(status);
        user.setUpdateTime(new Date());

        // 保存用户
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(Integer userId, String oldPassword, String newPassword) {
        // 获取用户信息
        User user = getById(userId);

        // 验证旧密码
        if (!MD5Util.verify(oldPassword, user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }

        // 更新密码
        user.setPassword(MD5Util.encrypt(newPassword));
        user.setUpdateTime(new Date());

        // 保存用户
        userRepository.save(user);
    }
} 