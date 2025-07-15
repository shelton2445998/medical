package com.fourth.medical.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 聊天消息查询结果
 *
 * @author fourth
 * @since 2025-07-12
 */
@Data
@Schema(description = "聊天消息查询结果")
public class ChatMessageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "是否用户消息")
    private Boolean isUser;

    @Schema(description = "时间")
    private String time;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "会话ID")
    private String sessionId;

    @Schema(description = "AI医生ID")
    private Long doctorId;

    @Schema(description = "AI医生姓名")
    private String doctorName;

    @Schema(description = "AI医生头像")
    private String doctorAvatar;

    @Schema(description = "创建时间")
    private Date createTime;
} 