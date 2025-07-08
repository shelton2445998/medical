package com.forth.medicalreserve.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 科室实体类
 */
@Data
@Entity
@Table(name = "department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 科室ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "departmentId")
    private Integer departmentId;

    /**
     * 科室名称
     */
    @Column(name = "departmentName", nullable = false)
    private String departmentName;
    
    /**
     * 科室描述
     */
    @Column(name = "description")
    private String description;
    
    /**
     * 所属医院ID
     */
    @Column(name = "hospitalId")
    private Integer hospitalId;
    
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