package com.forth.medicalreserve.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 医生实体类
 */
@Data
@Entity
@Table(name = "doctor")
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 医生ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctorId")
    private Integer doctorId;

    /**
     * 医生姓名
     */
    @Column(name = "doctorName", nullable = false)
    private String doctorName;

    /**
     * 密码（加密存储）
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * 性别：0-女，1-男
     */
    @Column(name = "gender")
    private Integer gender;

    /**
     * 手机号
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 所属医院ID
     */
    @Column(name = "hospitalId")
    private Integer hospitalId;
    
    /**
     * 科室ID
     */
    @Column(name = "departmentId")
    private Integer departmentId;
    
    /**
     * 职称
     */
    @Column(name = "title")
    private String title;

    /**
     * 简介
     */
    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

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
    
    // 不使用ORM关联，改为手动查询
    @Transient
    private Hospital hospital;
    
    @Transient
    private Department department;
} 