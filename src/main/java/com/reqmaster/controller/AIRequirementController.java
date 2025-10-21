package com.reqmaster.controller;

import com.reqmaster.dto.ApiResponse;
import com.reqmaster.entity.Requirement;
import com.reqmaster.service.AIRequirementService;
import com.reqmaster.service.llm.LLMService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * AI需求分析控制器
 */
@RestController
@RequestMapping("/api/ai/requirements")
@RequiredArgsConstructor
@Tag(name = "AI需求分析", description = "基于大模型的智能需求分析功能")
public class AIRequirementController {

    private final AIRequirementService aiRequirementService;

    @PostMapping("/parse-document")
    @Operation(summary = "智能解析文档并创建需求")
    public ResponseEntity<ApiResponse<Requirement>> parseDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("projectId") Long projectId) {
        Requirement requirement = aiRequirementService.parseDocumentAndCreateRequirements(file, projectId);
        return ResponseEntity.ok(ApiResponse.success("文档解析成功", requirement));
    }

    @GetMapping("/{requirementId}/user-stories")
    @Operation(summary = "生成用户故事")
    public ResponseEntity<ApiResponse<List<LLMService.UserStory>>> generateUserStories(
            @PathVariable Long requirementId) {
        List<LLMService.UserStory> userStories = aiRequirementService.generateUserStories(requirementId);
        return ResponseEntity.ok(ApiResponse.success("用户故事生成成功", userStories));
    }

    @GetMapping("/project/{projectId}/consistency")
    @Operation(summary = "检查需求一致性")
    public ResponseEntity<ApiResponse<List<LLMService.RequirementIssue>>> checkConsistency(
            @PathVariable Long projectId) {
        List<LLMService.RequirementIssue> issues = aiRequirementService.checkConsistency(projectId);
        return ResponseEntity.ok(ApiResponse.success("一致性检查完成", issues));
    }

    @GetMapping("/{requirementId}/completeness")
    @Operation(summary = "分析需求完整性")
    public ResponseEntity<ApiResponse<LLMService.CompletenessAnalysis>> analyzeCompleteness(
            @PathVariable Long requirementId) {
        LLMService.CompletenessAnalysis analysis = aiRequirementService.analyzeCompleteness(requirementId);
        return ResponseEntity.ok(ApiResponse.success("完整性分析完成", analysis));
    }

    @PostMapping("/project/{projectId}/chat")
    @Operation(summary = "智能对话分析")
    public ResponseEntity<ApiResponse<String>> chatAnalysis(
            @PathVariable Long projectId,
            @RequestParam String question) {
        String response = aiRequirementService.chatAnalysis(question, projectId);
        return ResponseEntity.ok(ApiResponse.success("分析完成", response));
    }
}