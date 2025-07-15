package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 聊天消息参数
 *
 * @author fourth
 * @since 2025-07-12
 */
@Data
@Schema(description = "聊天消息参数")
public class ChatMessageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "消息内容")
    @NotBlank(message = "消息内容不能为空")
    @Length(max = 1000, message = "消息内容长度超过限制")
    private String message;

    @Schema(description = "消息类型 1:用户消息，2:AI回复")
    private Integer type;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "会话ID")
    private String sessionId;
} 