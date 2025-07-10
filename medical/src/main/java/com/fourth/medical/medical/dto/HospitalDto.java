package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 医院参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "医院参数")
public class HospitalDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "医院名称")
    @NotBlank(message = "医院名称不能为空")
    @Length(max = 100, message = "医院名称长度超过限制")
    private String name;

    @Schema(description = "医院电话")
    private String phone;

    @Schema(description = "医院地址")
    private String address;

    @Schema(description = "医院简介")
    private String description;

    @Schema(description = "状态 0:禁用，1:启用")
    private Boolean status;

}


