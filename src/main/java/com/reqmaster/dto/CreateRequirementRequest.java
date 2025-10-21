package com.reqmaster.dto;

import com.reqmaster.entity.enums.RequirementType;
import com.reqmaster.entity.enums.Priority;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 创建需求请求DTO
 */
@Data
public class CreateRequirementRequest {

    @NotBlank(message = "需求标题不能为空")
    @Size(max = 200, message = "需求标题长度不能超过200个字符")
    private String title;

    @Size(max = 5000, message = "需求描述长度不能超过5000个字符")
    private String description;

    @NotNull(message = "需求类型不能为空")
    private RequirementType type;

    @NotNull(message = "优先级不能为空")
    private Priority priority;

    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    private String sourceType = "MANUAL";
}