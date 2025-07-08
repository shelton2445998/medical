package com.forth.medicalreserve.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 */
@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Integer userId;

    /**
     * 用户名
     */
    @Column(name = "userName", nullable = false)
    private String userName;

    /**
     * 密码（加密存储）
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * 真实姓名
     */
    @Column(name = "realName")
    private String realName;

    /**
     * 性别：0-女，1-男
     */
    @Column(name = "gender")
    private Integer gender;

    /**
     * 身份证号
     */
    @Column(name = "idCard")
    private String idCard;

    /**
     * 手机号
     */
    @Column(name = "mobile", nullable = false)
    private String mobile;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 状态：0-禁用，1-启用
     */
    @Column(name = "status", nullable = false)
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "createTime", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
} 