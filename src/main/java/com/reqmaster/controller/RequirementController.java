package com.reqmaster.controller;

import com.reqmaster.dto.ApiResponse;
import com.reqmaster.dto.CreateRequirementRequest;
import com.reqmaster.dto.RequirementDTO;
import com.reqmaster.entity.enums.RequirementType;
import com.reqmaster.entity.enums.Priority;
import com.reqmaster.service.RequirementService;
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
 * 需求控制器
 */
@RestController
@RequestMapping("/api/requirements")
@RequiredArgsConstructor
@Tag(name = "需求管理", description = "需求的增删改查和相关操作")
public class RequirementController {

    private final RequirementService requirementService;

    @PostMapping
    @Operation(summary = "创建需求")
    public ResponseEntity<ApiResponse<RequirementDTO>> createRequirement(@Valid @RequestBody CreateRequirementRequest request) {
        RequirementDTO requirement = requirementService.createRequirement(request);
        return ResponseEntity.ok(ApiResponse.success("需求创建成功", requirement));
    }

    @GetMapping
    @Operation(summary = "获取所有需求")
    public ResponseEntity<ApiResponse<List<RequirementDTO>>> getAllRequirements() {
        List<RequirementDTO> requirements = requirementService.getAllRequirements();
        return ResponseEntity.ok(ApiResponse.success(requirements));
    }

    @GetMapping("/page")
    @Operation(summary = "分页获取需求")
    public ResponseEntity<ApiResponse<Page<RequirementDTO>>> getRequirements(@ParameterObject Pageable pageable) {
        Page<RequirementDTO> requirements = requirementService.getRequirements(pageable);
        return ResponseEntity.ok(ApiResponse.success(requirements));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取需求")
    public ResponseEntity<ApiResponse<RequirementDTO>> getRequirementById(@PathVariable Long id) {
        RequirementDTO requirement = requirementService.getRequirementById(id);
        return ResponseEntity.ok(ApiResponse.success(requirement));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新需求")
    public ResponseEntity<ApiResponse<RequirementDTO>> updateRequirement(
            @PathVariable Long id,
            @Valid @RequestBody CreateRequirementRequest request) {
        RequirementDTO requirement = requirementService.updateRequirement(id, request);
        return ResponseEntity.ok(ApiResponse.success("需求更新成功", requirement));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除需求")
    public ResponseEntity<ApiResponse<Void>> deleteRequirement(@PathVariable Long id) {
        requirementService.deleteRequirement(id);
        return ResponseEntity.ok(ApiResponse.success("需求删除成功", null));
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "根据项目获取需求")
    public ResponseEntity<ApiResponse<List<RequirementDTO>>> getRequirementsByProject(@PathVariable Long projectId) {
        List<RequirementDTO> requirements = requirementService.getRequirementsByProject(projectId);
        return ResponseEntity.ok(ApiResponse.success(requirements));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "根据类型获取需求")
    public ResponseEntity<ApiResponse<List<RequirementDTO>>> getRequirementsByType(@PathVariable RequirementType type) {
        List<RequirementDTO> requirements = requirementService.getRequirementsByType(type);
        return ResponseEntity.ok(ApiResponse.success(requirements));
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "根据优先级获取需求")
    public ResponseEntity<ApiResponse<List<RequirementDTO>>> getRequirementsByPriority(@PathVariable Priority priority) {
        List<RequirementDTO> requirements = requirementService.getRequirementsByPriority(priority);
        return ResponseEntity.ok(ApiResponse.success(requirements));
    }

    @GetMapping("/search")
    @Operation(summary = "搜索需求")
    public ResponseEntity<ApiResponse<List<RequirementDTO>>> searchRequirements(@RequestParam String keyword) {
        List<RequirementDTO> requirements = requirementService.searchRequirements(keyword);
        return ResponseEntity.ok(ApiResponse.success(requirements));
    }

    @GetMapping("/project/{projectId}/stats")
    @Operation(summary = "获取项目需求统计")
    public ResponseEntity<ApiResponse<RequirementService.RequirementStats>> getRequirementStats(@PathVariable Long projectId) {
        RequirementService.RequirementStats stats = requirementService.getRequirementStats(projectId);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @PutMapping("/{id}/analyzed")
    @Operation(summary = "标记需求为已分析")
    public ResponseEntity<ApiResponse<RequirementDTO>> markAsAnalyzed(@PathVariable Long id) {
        RequirementDTO requirement = requirementService.markAsAnalyzed(id);
        return ResponseEntity.ok(ApiResponse.success("需求标记为已分析", requirement));
    }
}