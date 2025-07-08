package com.forth.medicalreserve.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 医院实体类
 */
@Data
@Entity
@Table(name = "hospital")
public class Hospital implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 医院ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospitalId")
    private Integer hospitalId;

    /**
     * 医院名称
     */
    @Column(name = "hospitalName", nullable = false)
    private String hospitalName;

    /**
     * 医院电话
     */
    @Column(name = "hospitalPhone")
    private String hospitalPhone;

    /**
     * 医院地址
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