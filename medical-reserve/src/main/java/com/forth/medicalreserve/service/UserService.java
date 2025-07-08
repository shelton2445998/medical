package com.forth.medicalreserve.service;

import com.forth.medicalreserve.entity.Users;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册成功的用户
     */
    Users register(Users user);
    
    /**
     * 用户登录
     * @param userName 用户名
     * @param password 密码
     * @return 登录成功的用户
     */
    Users login(String userName, String password);
    
    /**
     * 手机号验证码登录
     * @param mobile 手机号
     * @param code 验证码
     * @return 登录成功的用户
     */
    Users loginByMobile(String mobile, String code);
    
    /**
     * 根据ID查询用户
     * @param userId 用户ID
     * @return 用户信息
     */
    Users findById(Integer userId);
    
    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新后的用户
     */
    Users updateUser(Users user);
    
    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean changePassword(Integer userId, String oldPassword, String newPassword);
    
    /**
     * 重置密码
     * @param mobile 手机号
     * @param code 验证码
     * @param newPassword 新密码
     * @return 是否重置成功
     */
    boolean resetPassword(String mobile, String code, String newPassword);
    
    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<Users> findAll();
    
    /**
     * 禁用/启用用户
     * @param userId 用户ID
     * @param status 状态
     * @return 是否操作成功
     */
    boolean updateStatus(Integer userId, Integer status);
} 