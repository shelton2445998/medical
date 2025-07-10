package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统通知参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "系统通知参数")
public class NotificationDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "接收用户ID")
    private Long userId;

    @Schema(description = "接收医生ID")
    private Long doctorId;

    @Schema(description = "通知标题")
    @NotBlank(message = "通知标题不能为空")
    @Length(max = 100, message = "通知标题长度超过限制")
    private String title;

    @Schema(description = "通知内容")
    private String content;

    @Schema(description = "通知类型 1:预约提醒，2:报告通知，3:系统公告")
    @NotNull(message = "通知类型不能为空")
    private Integer type;

    @Schema(description = "是否已读 0:未读，1:已读")
    private Boolean isRead;

}


