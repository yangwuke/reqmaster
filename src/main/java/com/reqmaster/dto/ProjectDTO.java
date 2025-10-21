package com.reqmaster.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 项目数据传输对象
 */
@Data
public class ProjectDTO {

    private Long id;

    @NotBlank(message = "项目名称不能为空")
    @Size(max = 100, message = "项目名称长度不能超过100个字符")
    private String name;

    @Size(max = 1000, message = "项目描述长度不能超过1000个字符")
    private String description;

    private String domain;

    private Integer requirementCount;

    // 转换方法
    public static ProjectDTO fromEntity(com.reqmaster.entity.Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setDomain(project.getDomain());
        dto.setRequirementCount(project.getRequirements() != null ? project.getRequirements().size() : 0);
        return dto;
    }
}