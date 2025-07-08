package com.forth.medicalreserve.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 检查项实体类
 */
@Data
@Entity
@Table(name = "checkitem")
public class CheckItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 检查项ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemId")
    private Integer itemId;

    /**
     * 检查项名称
     */
    @Column(name = "itemName", nullable = false)
    private String itemName;
    
    /**
     * 科室ID
     */
    @Column(name = "departmentId")
    private Integer departmentId;
    
    /**
     * 价格
     */
    @Column(name = "price")
    private BigDecimal price;
    
    /**
     * 描述
     */
    @Column(name = "description")
    private String description;
    
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
    private Department department;
} 