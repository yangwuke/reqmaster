package com.reqmaster.service;

import com.reqmaster.dto.CreateProjectRequest;
import com.reqmaster.dto.ProjectDTO;
import com.reqmaster.entity.Project;
import com.reqmaster.exception.BusinessException;
import com.reqmaster.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;

    /**
     * 创建项目
     */
    @Transactional
    public ProjectDTO createProject(CreateProjectRequest request) {
        // 检查项目名称是否已存在
        if (projectRepository.findByName(request.getName()).isPresent()) {
            throw new BusinessException("项目名称已存在: " + request.getName());
        }

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setDomain(request.getDomain());

        Project savedProject = projectRepository.save(project);
        log.info("创建项目成功: {}", savedProject.getName());

        return ProjectDTO.fromEntity(savedProject);
    }

    /**
     * 获取所有项目
     */
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 分页获取项目
     */
    public Page<ProjectDTO> getProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(ProjectDTO::fromEntity);
    }

    /**
     * 根据ID获取项目
     */
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException("项目不存在: " + id));
        return ProjectDTO.fromEntity(project);
    }

    /**
     * 更新项目
     */
    @Transactional
    public ProjectDTO updateProject(Long id, CreateProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException("项目不存在: " + id));

        // 检查名称是否被其他项目使用
        projectRepository.findByName(request.getName())
                .ifPresent(existingProject -> {
                    if (!existingProject.getId().equals(id)) {
                        throw new BusinessException("项目名称已被其他项目使用: " + request.getName());
                    }
                });

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setDomain(request.getDomain());

        Project updatedProject = projectRepository.save(project);
        log.info("更新项目成功: {}", updatedProject.getName());

        return ProjectDTO.fromEntity(updatedProject);
    }

    /**
     * 删除项目
     */
    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException("项目不存在: " + id));

        projectRepository.delete(project);
        log.info("删除项目成功: {}", project.getName());
    }

    /**
     * 搜索项目
     */
    public List<ProjectDTO> searchProjects(String keyword) {
        return projectRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(ProjectDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 根据领域获取项目
     */
    public List<ProjectDTO> getProjectsByDomain(String domain) {
        return projectRepository.findByDomain(domain).stream()
                .map(ProjectDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 获取项目统计
     */
    public ProjectStats getProjectStats() {
        Long totalProjects = projectRepository.countAllProjects();
        // 这里可以添加更多统计信息
        return new ProjectStats(totalProjects, 0L, 0L);
    }

    // 项目统计内部类
    public record ProjectStats(Long totalProjects, Long activeProjects, Long totalRequirements) {}
}