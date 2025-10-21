package com.reqmaster.service;

import com.reqmaster.dto.CreateProjectRequest;
import com.reqmaster.dto.ProjectDTO;
import com.reqmaster.entity.Project;
import com.reqmaster.exception.BusinessException;
import com.reqmaster.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private CreateProjectRequest createProjectRequest;

    @BeforeEach
    void setUp() {
        createProjectRequest = new CreateProjectRequest();
        createProjectRequest.setName("测试项目");
        createProjectRequest.setDescription("测试描述");
        createProjectRequest.setDomain("测试领域");
    }

    @Test
    void createProject_Success() {
        // 准备
        when(projectRepository.findByName("测试项目")).thenReturn(Optional.empty());

        Project savedProject = new Project();
        savedProject.setId(1L);
        savedProject.setName("测试项目");
        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);

        // 执行
        ProjectDTO result = projectService.createProject(createProjectRequest);

        // 验证
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试项目", result.getName());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void createProject_ProjectExists_ThrowsException() {
        // 准备
        Project existingProject = new Project();
        when(projectRepository.findByName("测试项目")).thenReturn(Optional.of(existingProject));

        // 执行和验证
        assertThrows(BusinessException.class, () -> projectService.createProject(createProjectRequest));
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void getProjectById_Success() {
        // 准备
        Project project = new Project();
        project.setId(1L);
        project.setName("测试项目");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // 执行
        ProjectDTO result = projectService.getProjectById(1L);

        // 验证
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试项目", result.getName());
    }

    @Test
    void getProjectById_NotFound_ThrowsException() {
        // 准备
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行和验证
        assertThrows(BusinessException.class, () -> projectService.getProjectById(1L));
    }
}