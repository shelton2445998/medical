package com.forth.medicalreserve.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 体检套餐实体类
 */
@Data
@Entity
@Table(name = "setmeal")
public class SetMeal implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 套餐ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "packageId")
    private Integer packageId;

    /**
     * 套餐名称
     */
    @Column(name = "packageName", nullable = false)
    private String packageName;
    
    /**
     * 套餐类型：1-基础，2-高级，3-专项
     */
    @Column(name = "packageType")
    private Integer packageType;
    
    /**
     * 价格
     */
    @Column(name = "price")
    private BigDecimal price;
    
    /**
     * 折扣价
     */
    @Column(name = "discountPrice")
    private BigDecimal discountPrice;
    
    /**
     * 描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * 状态：0-下架，1-上架
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