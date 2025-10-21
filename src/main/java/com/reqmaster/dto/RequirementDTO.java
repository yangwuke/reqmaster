package com.reqmaster.dto;

import com.reqmaster.entity.enums.RequirementType;
import com.reqmaster.entity.enums.Priority;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 需求数据传输对象
 */
@Data
public class RequirementDTO {

    private Long id;

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

    private String projectName; // 用于显示

    private String sourceType;

    private Boolean isAnalyzed = false;

    private String metadata;

    // 转换方法
    public static RequirementDTO fromEntity(com.reqmaster.entity.Requirement requirement) {
        RequirementDTO dto = new RequirementDTO();
        dto.setId(requirement.getId());
        dto.setTitle(requirement.getTitle());
        dto.setDescription(requirement.getDescription());
        dto.setType(requirement.getType());
        dto.setPriority(requirement.getPriority());
        dto.setProjectId(requirement.getProject().getId());
        dto.setProjectName(requirement.getProject().getName());
        dto.setSourceType(requirement.getSourceType());
        dto.setIsAnalyzed(requirement.getIsAnalyzed());
        dto.setMetadata(requirement.getMetadata());
        return dto;
    }
}