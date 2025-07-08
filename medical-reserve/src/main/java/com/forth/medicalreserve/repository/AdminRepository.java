package com.forth.medicalreserve.repository;

import com.forth.medicalreserve.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 管理员数据访问层
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    /**
     * 根据用户名查找管理员
     * @param username 用户名
     * @return 管理员
     */
    Optional<Admin> findByUsername(String username);

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
} 