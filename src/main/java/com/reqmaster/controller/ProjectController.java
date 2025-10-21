package com.reqmaster.controller;

import com.reqmaster.dto.ApiResponse;
import com.reqmaster.dto.CreateProjectRequest;
import com.reqmaster.dto.ProjectDTO;
import com.reqmaster.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目控制器
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "项目管理", description = "项目的增删改查和相关操作")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "创建项目")
    public ResponseEntity<ApiResponse<ProjectDTO>> createProject(@Valid @RequestBody CreateProjectRequest request) {
        ProjectDTO project = projectService.createProject(request);
        return ResponseEntity.ok(ApiResponse.success("项目创建成功", project));
    }

    @GetMapping
    @Operation(summary = "获取所有项目")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(ApiResponse.success(projects));
    }

    @GetMapping("/page")
    @Operation(summary = "分页获取项目")
    public ResponseEntity<ApiResponse<Page<ProjectDTO>>> getProjects(@ParameterObject Pageable pageable) {
        Page<ProjectDTO> projects = projectService.getProjects(pageable);
        return ResponseEntity.ok(ApiResponse.success(projects));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取项目")
    public ResponseEntity<ApiResponse<ProjectDTO>> getProjectById(@PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(ApiResponse.success(project));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新项目")
    public ResponseEntity<ApiResponse<ProjectDTO>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody CreateProjectRequest request) {
        ProjectDTO project = projectService.updateProject(id, request);
        return ResponseEntity.ok(ApiResponse.success("项目更新成功", project));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除项目")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.success("项目删除成功", null));
    }

    @GetMapping("/search")
    @Operation(summary = "搜索项目")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> searchProjects(@RequestParam String keyword) {
        List<ProjectDTO> projects = projectService.searchProjects(keyword);
        return ResponseEntity.ok(ApiResponse.success(projects));
    }

    @GetMapping("/domain/{domain}")
    @Operation(summary = "根据领域获取项目")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getProjectsByDomain(@PathVariable String domain) {
        List<ProjectDTO> projects = projectService.getProjectsByDomain(domain);
        return ResponseEntity.ok(ApiResponse.success(projects));
    }

    @GetMapping("/stats")
    @Operation(summary = "获取项目统计")
    public ResponseEntity<ApiResponse<ProjectService.ProjectStats>> getProjectStats() {
        ProjectService.ProjectStats stats = projectService.getProjectStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}