package com.forth.medicalreserve.repository;

import com.forth.medicalreserve.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 根据用户名查找用户
     * @param userName 用户名
     * @return 用户
     */
    Optional<User> findByUserName(String userName);

    /**
     * 根据手机号查找用户
     * @param mobile 手机号
     * @return 用户
     */
    Optional<User> findByMobile(String mobile);

    /**
     * 检查用户名是否存在
     * @param userName 用户名
     * @return 是否存在
     */
    boolean existsByUserName(String userName);

    /**
     * 检查手机号是否存在
     * @param mobile 手机号
     * @return 是否存在
     */
    boolean existsByMobile(String mobile);
} 