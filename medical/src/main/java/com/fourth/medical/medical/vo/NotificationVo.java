package com.fourth.medical.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统通知查询结果
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "系统通知查询结果")
public class NotificationVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "接收用户ID")
    private Long userId;

    @Schema(description = "接收医生ID")
    private Long doctorId;

    @Schema(description = "通知标题")
    private String title;

    @Schema(description = "通知内容")
    private String content;

    @Schema(description = "通知类型 1:预约提醒，2:报告通知，3:系统公告")
    private Integer type;

    @Schema(description = "是否已读 0:未读，1:已读")
    private Boolean isRead;

    @Schema(description = "创建人ID")
    private Long createId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改人ID")
    private Long updateId;

    @Schema(description = "修改时间")
    private Date updateTime;

}

