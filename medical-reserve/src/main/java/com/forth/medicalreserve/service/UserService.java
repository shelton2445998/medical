package com.forth.medicalreserve.service;

import com.forth.medicalreserve.dto.UserDTO;
import com.forth.medicalreserve.entity.User;
import org.springframework.data.domain.Page;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 根据ID查询用户
     * @param userId 用户ID
     * @return 用户信息
     */
    User getById(Integer userId);

    /**
     * 根据用户名查询用户
     * @param userName 用户名
     * @return 用户信息
     */
    User getByUserName(String userName);

    /**
     * 根据手机号查询用户
     * @param mobile 手机号
     * @return 用户信息
     */
    User getByMobile(String mobile);

    /**
     * 分页查询用户列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param keyword 关键词
     * @return 用户分页列表
     */
    Page<User> page(int pageNum, int pageSize, String keyword);

    /**
     * 创建用户
     * @param userDTO 用户DTO
     * @return 创建后的用户ID
     */
    Integer create(UserDTO userDTO);

    /**
     * 更新用户
     * @param userId 用户ID
     * @param userDTO 用户DTO
     */
    void update(Integer userId, UserDTO userDTO);

    /**
     * 删除用户
     * @param userId 用户ID
     */
    void delete(Integer userId);

    /**
     * 禁用/启用用户
     * @param userId 用户ID
     * @param status 状态：0-禁用，1-启用
     */
    void updateStatus(Integer userId, Integer status);

    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void updatePassword(Integer userId, String oldPassword, String newPassword);
} 