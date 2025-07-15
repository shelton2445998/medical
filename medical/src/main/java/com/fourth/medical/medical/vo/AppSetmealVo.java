package com.fourth.medical.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * App体检套餐查询结果
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "App体检套餐查询结果")
public class AppSetmealVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "套餐名称")
    private String name;

    @Schema(description = "套餐类型 1:基础，2:高级，3:专项")
    private Integer type;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "折扣价")
    private BigDecimal discountPrice;

    @Schema(description = "描述（包含套餐介绍、适用人群、预约须知、用户评价，用分隔符分隔）")
    private String description;

    @Schema(description = "检查项ID列表，多个ID用逗号分隔")
    private String checkitemIds;

    @Schema(description = "状态 0:下架，1:上架")
    private Boolean status;

    @Schema(description = "创建人ID")
    private Long createId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改人ID")
    private Long updateId;

    @Schema(description = "修改时间")
    private Date updateTime;

    /**
     * 解析描述字段，获取套餐介绍
     * 描述字段格式：套餐介绍|适用人群|预约须知|用户评价
     */
    public String getPackageDescription() {
        if (description == null || description.isEmpty()) {
            return "";
        }
        String[] parts = description.split("\\|");
        return parts.length > 0 ? parts[0] : "";
    }

    /**
     * 解析描述字段，获取适用人群
     */
    public String getSuitableCrowd() {
        if (description == null || description.isEmpty()) {
            return "";
        }
        String[] parts = description.split("\\|");
        return parts.length > 1 ? parts[1] : "";
    }

    /**
     * 解析描述字段，获取预约须知
     */
    public String getAppointmentNotice() {
        if (description == null || description.isEmpty()) {
            return "";
        }
        String[] parts = description.split("\\|");
        return parts.length > 2 ? parts[2] : "";
    }

    /**
     * 解析描述字段，获取用户评价
     */
    public String getUserReviews() {
        if (description == null || description.isEmpty()) {
            return "";
        }
        String[] parts = description.split("\\|");
        return parts.length > 3 ? parts[3] : "";
    }
}
