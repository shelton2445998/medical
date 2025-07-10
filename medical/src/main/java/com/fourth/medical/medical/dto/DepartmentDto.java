package com.fourth.medical.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 科室参数
 *
 * @author fourth
 * @since 2025-07-09
 */
@Data
@Schema(description = "科室参数")
public class DepartmentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "科室名称")
    @NotBlank(message = "科室名称不能为空")
    @Length(max = 50, message = "科室名称长度超过限制")
    private String name;

    @Schema(description = "所属医院ID")
    private Long hospitalId;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "状态 0:禁用，1:启用")
    private Boolean status;

}


