package com.forth.medicalreserve.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 体检报告检查项明细实体类
 */
@Data
@Entity
@Table(name = "cidetailedreport")
public class CIDetailedReport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 明细ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailId")
    private Integer detailId;

    /**
     * 报告ID
     */
    @Column(name = "reportId")
    private Integer reportId;
    
    /**
     * 检查项明细ID
     */
    @Column(name = "detailItemId")
    private Integer detailItemId;
    
    /**
     * 检查值
     */
    @Column(name = "value")
    private String value;
    
    /**
     * 是否异常：0-正常，1-异常
     */
    @Column(name = "isAbnormal")
    private Integer isAbnormal;
    
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
    private CIReport ciReport;
    
    @Transient
    private CheckItemDetailed checkItemDetailed;
} 