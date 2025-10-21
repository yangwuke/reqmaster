package com.reqmaster.service;

import com.reqmaster.dto.CreateRequirementRequest;
import com.reqmaster.dto.RequirementDTO;
import com.reqmaster.entity.Project;
import com.reqmaster.entity.Requirement;
import com.reqmaster.entity.enums.RequirementType;
import com.reqmaster.entity.enums.Priority;
import com.reqmaster.exception.BusinessException;
import com.reqmaster.repository.ProjectRepository;
import com.reqmaster.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 需求服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RequirementService {

    private final RequirementRepository requirementRepository;
    private final ProjectRepository projectRepository;

    /**
     * 创建需求
     */
    @Transactional
    public RequirementDTO createRequirement(CreateRequirementRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new BusinessException("项目不存在: " + request.getProjectId()));

        Requirement requirement = new Requirement();
        requirement.setTitle(request.getTitle());
        requirement.setDescription(request.getDescription());
        requirement.setType(request.getType());
        requirement.setPriority(request.getPriority());
        requirement.setProject(project);
        requirement.setSourceType(request.getSourceType());

        Requirement savedRequirement = requirementRepository.save(requirement);
        log.info("创建需求成功: {}", savedRequirement.getTitle());

        return RequirementDTO.fromEntity(savedRequirement);
    }

    /**
     * 获取所有需求
     */
    public List<RequirementDTO> getAllRequirements() {
        return requirementRepository.findAll().stream()
                .map(RequirementDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 分页获取需求
     */
    public Page<RequirementDTO> getRequirements(Pageable pageable) {
        return requirementRepository.findAll(pageable)
                .map(RequirementDTO::fromEntity);
    }

    /**
     * 根据ID获取需求
     */
    public RequirementDTO getRequirementById(Long id) {
        Requirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new BusinessException("需求不存在: " + id));
        return RequirementDTO.fromEntity(requirement);
    }

    /**
     * 更新需求
     */
    @Transactional
    public RequirementDTO updateRequirement(Long id, CreateRequirementRequest request) {
        Requirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new BusinessException("需求不存在: " + id));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new BusinessException("项目不存在: " + request.getProjectId()));

        requirement.setTitle(request.getTitle());
        requirement.setDescription(request.getDescription());
        requirement.setType(request.getType());
        requirement.setPriority(request.getPriority());
        requirement.setProject(project);
        requirement.setSourceType(request.getSourceType());

        Requirement updatedRequirement = requirementRepository.save(requirement);
        log.info("更新需求成功: {}", updatedRequirement.getTitle());

        return RequirementDTO.fromEntity(updatedRequirement);
    }

    /**
     * 删除需求
     */
    @Transactional
    public void deleteRequirement(Long id) {
        Requirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new BusinessException("需求不存在: " + id));

        requirementRepository.delete(requirement);
        log.info("删除需求成功: {}", requirement.getTitle());
    }

    /**
     * 根据项目ID获取需求
     */
    public List<RequirementDTO> getRequirementsByProject(Long projectId) {
        return requirementRepository.findByProjectId(projectId).stream()
                .map(RequirementDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 根据类型获取需求
     */
    public List<RequirementDTO> getRequirementsByType(RequirementType type) {
        return requirementRepository.findByType(type).stream()
                .map(RequirementDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 根据优先级获取需求
     */
    public List<RequirementDTO> getRequirementsByPriority(Priority priority) {
        return requirementRepository.findByPriority(priority).stream()
                .map(RequirementDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 搜索需求
     */
    public List<RequirementDTO> searchRequirements(String keyword) {
        return requirementRepository.findByTitleContainingIgnoreCase(keyword).stream()
                .map(RequirementDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 获取项目需求统计
     */
    public RequirementStats getRequirementStats(Long projectId) {
        Long totalRequirements = requirementRepository.countByProjectId(projectId);
        Long analyzedRequirements = requirementRepository.findByProjectId(projectId).stream()
                .filter(Requirement::getIsAnalyzed)
                .count();

        return new RequirementStats(totalRequirements, analyzedRequirements);
    }

    /**
     * 标记需求为已分析
     */
    @Transactional
    public RequirementDTO markAsAnalyzed(Long id) {
        Requirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new BusinessException("需求不存在: " + id));

        requirement.setIsAnalyzed(true);
        Requirement updatedRequirement = requirementRepository.save(requirement);

        return RequirementDTO.fromEntity(updatedRequirement);
    }

    // 需求统计内部类
    public record RequirementStats(Long totalRequirements, Long analyzedRequirements) {}
}