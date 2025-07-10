package com.fourth.medical.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 家庭成员查询结果
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "家庭成员查询结果")
public class FamilyMemberVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "关联用户ID")
    private Long userId;

    @Schema(description = "成员姓名")
    private String name;

    @Schema(description = "性别 0:女，1:男")
    private Boolean gender;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "与用户关系")
    private String relation;

    @Schema(description = "创建人ID")
    private Long createId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改人ID")
    private Long updateId;

    @Schema(description = "修改时间")
    private Date updateTime;

}

