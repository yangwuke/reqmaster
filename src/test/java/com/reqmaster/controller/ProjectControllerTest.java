package com.reqmaster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reqmaster.dto.CreateProjectRequest;
import com.reqmaster.entity.Project;
import com.reqmaster.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Project testProject;

    @BeforeEach
    void setUp() {
        // 清理数据
        projectRepository.deleteAll();

        // 创建测试项目
        testProject = new Project();
        testProject.setName("测试项目");
        testProject.setDescription("测试描述");
        testProject.setDomain("测试领域");
        testProject = projectRepository.save(testProject);
    }

    @Test
    void createProject_Success() throws Exception {
        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("新项目");
        request.setDescription("新项目描述");
        request.setDomain("电商");

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("新项目"));
    }

    @Test
    void getAllProjects_Success() throws Exception {
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    void getProjectById_Success() throws Exception {
        mockMvc.perform(get("/api/projects/{id}", testProject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(testProject.getId()));
    }

    @Test
    void updateProject_Success() throws Exception {
        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("更新后的项目");
        request.setDescription("更新后的描述");
        request.setDomain("金融");

        mockMvc.perform(put("/api/projects/{id}", testProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("更新后的项目"));
    }

    @Test
    void deleteProject_Success() throws Exception {
        mockMvc.perform(delete("/api/projects/{id}", testProject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}