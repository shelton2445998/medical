package com.fourth.medical.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 医院查询结果
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "医院查询结果")
public class HospitalVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "医院名称")
    private String name;

    @Schema(description = "医院电话")
    private String phone;

    @Schema(description = "医院地址")
    private String address;

    @Schema(description = "医院简介")
    private String description;

    @Schema(description = "状态 0:禁用，1:启用")
    private Boolean status;

    @Schema(description = "创建人ID")
    private Long createId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改人ID")
    private Long updateId;

    @Schema(description = "修改时间")
    private Date updateTime;

}

