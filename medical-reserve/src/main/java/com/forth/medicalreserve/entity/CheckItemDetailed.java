package com.forth.medicalreserve.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 检查项明细实体类
 */
@Data
@Entity
@Table(name = "checkitemdetailed")
public class CheckItemDetailed implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 明细ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailId")
    private Integer detailId;

    /**
     * 检查项ID
     */
    @Column(name = "itemId")
    private Integer itemId;
    
    /**
     * 明细名称
     */
    @Column(name = "detailName")
    private String detailName;
    
    /**
     * 正常值范围
     */
    @Column(name = "normalValue")
    private String normalValue;
    
    /**
     * 单位
     */
    @Column(name = "unit")
    private String unit;
    
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
    private CheckItem checkItem;
} 