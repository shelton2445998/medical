package com.forth.medicalreserve.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 体检预约订单实体类
 */
@Data
@Entity
@Table(name = "orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private Integer orderId;

    /**
     * 用户ID
     */
    @Column(name = "userId")
    private Integer userId;
    
    /**
     * 套餐ID
     */
    @Column(name = "packageId")
    private Integer packageId;
    
    /**
     * 医院ID
     */
    @Column(name = "hospitalId")
    private Integer hospitalId;
    
    /**
     * 预约日期
     */
    @Column(name = "appointmentDate")
    @Temporal(TemporalType.DATE)
    private Date appointmentDate;
    
    /**
     * 时间段
     */
    @Column(name = "timeSlot")
    private String timeSlot;
    
    /**
     * 状态：0-已取消，1-待支付，2-已支付，3-已完成
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
    private Users user;
    
    @Transient
    private SetMeal setMeal;
    
    @Transient
    private Hospital hospital;
} 