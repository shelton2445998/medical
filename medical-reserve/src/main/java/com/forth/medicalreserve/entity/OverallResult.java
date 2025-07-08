package com.forth.medicalreserve.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 总检结论实体类
 */
@Data
@Entity
@Table(name = "overallresult")
public class OverallResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 结论ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resultId")
    private Integer resultId;

    /**
     * 订单ID
     */
    @Column(name = "orderId")
    private Integer orderId;
    
    /**
     * 结论内容
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    
    /**
     * 医生ID
     */
    @Column(name = "doctorId")
    private Integer doctorId;
    
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
    private Orders order;
    
    @Transient
    private Doctor doctor;
} 