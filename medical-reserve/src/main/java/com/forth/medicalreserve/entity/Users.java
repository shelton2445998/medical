package com.forth.medicalreserve.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false)
    private String userName;
    
    @Column(nullable = false)
    private String password;
    
    private String realName;
    
    /**
     * 性别: 0-女, 1-男
     */
    private Integer gender;
    
    private String idCard;
    
    @Column(nullable = false)
    private String mobile;
    
    private String email;
    
    private String address;
    
    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 